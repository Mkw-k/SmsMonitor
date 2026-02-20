def runJar(port) {
    echo "Starting JAR on port ${port}..."
    withCredentials([sshUserPrivateKey(credentialsId: "${SSH_CREDENTIALS_ID}", keyFileVariable: 'SSH_PRIVATE_KEY', usernameVariable: 'SSH_USER')]) {
        sh """
        ssh -i \$SSH_PRIVATE_KEY -o StrictHostKeyChecking=no \$SSH_USER@${SSH_SERVER} '
            nohup java -Duser.timezone=Asia/Seoul -jar ${REMOTE_DIR}/${env.LATEST_JAR} --server.port=${port} --spring.profiles.active=${DEPLOY_PROFILE} > ${REMOTE_DIR}/${LOG_FILE_NAME}.log 2>&1 &
            echo "JAR started on port ${port}."
            tail -f ${REMOTE_DIR}/${LOG_FILE_NAME}.log | while read line; do
                echo "\$line"
                if [[ "\$line" == *"${APP_START_MESSAGE}"* ]]; then
                    echo "JAR started successfully on port ${port}."
                    exit 0
                elif [[ "\$line" == *"APPLICATION FAILED TO START"* ]]; then
                    echo "JAR failed to start on port ${port}."
                    exit 1
                fi
            done
        '
        """
    }
}

    def shutdownJar(port, type) {
        echo "Shutting down ${type} JAR on port ${port}..."
        withCredentials([sshUserPrivateKey(credentialsId: "${SSH_CREDENTIALS_ID}", keyFileVariable: 'SSH_PRIVATE_KEY', usernameVariable: 'SSH_USER')]) {
            sh """
            ssh -i \$SSH_PRIVATE_KEY -o StrictHostKeyChecking=no \$SSH_USER@${SSH_SERVER} '
                PID=\$(lsof -t -i :${port} -s TCP:LISTEN)
                if [ "\$PID" != "" ]; then
                    kill -9 \$PID
                    echo "${type.capitalize()} JAR shutdown initiated."
                    if [ "\$(lsof -t -i :${port} -s TCP:LISTEN)" != "" ]; then
                        echo "Failed to shut down ${type} JAR on port ${port}."
                        exit 1
                    else
                        echo "Successfully shut down ${type} JAR on port ${port}."
                    fi
                else
                    echo "No ${type} application running on port ${port}."
                fi
            '
            """
        }
    }

pipeline {
    agent any

    tools {
        gradle 'my-gradle'
        git 'my-git' // Git 툴 추가
        maven 'my-maven'
    }

    environment {
        REPO_URL = 'https://github.com/Mkw-k/SmsMonitor.git'
        BRANCH = 'master'
        SSH_SERVER = '183.102.48.104'
        REMOTE_DIR = '/data/smsMonitorLog' // 원격 서버에서 파일을 저장할 경로
        REMOTE_USER = 'ubuntu'
        EXSISTED_PROC_PORT = '8080'
        NEW_PROC_PORT = '8081'
        JAR_PERFIX = 'SmsMonitor'
        OUTPUT_DIR = './SmsMonitor/target' // 변경할 출력 폴더
        DEPLOY_PROFILE = 'prod'
        APP_START_MESSAGE = 'Started SmsMonitorApplication' // 애플리케이션 시작 메시지를 변수로 설정
        LOG_FILE_NAME = 'spring-boot-new'
        SSH_CREDENTIALS_ID = 'web1-ssh2' // SSH 자격 증명 ID를 변수로 설정
        GIT_CREDENTIALS_ID = 'github-signin'
    }

    stages {
       stage('Checkout') {
            steps {
                script {
                    // Git 리포지토리 체크아웃
                    git branch: "${BRANCH}", url: "${REPO_URL}", credentialsId: "${GIT_CREDENTIALS_ID}"
                }
            }
        }

        stage('Build') {
            steps {
                script {
                    sh '''
                        mvn clean package -PjarPrefix=$JAR_PREFIX -PbuildTimestamp=$(date +%Y%m%d-%H%M) -DskipTests
                    '''
                }
            }
        }

        stage('Find Latest JAR') {
            steps {
                script {
                    echo "Find Latest JAR start!"
                    sh "pwd"
                    sh "ls"
                    sh "ls ${OUTPUT_DIR}"
                    env.LATEST_JAR = sh(script: "ls -t ${OUTPUT_DIR}/${JAR_PERFIX}-*.jar| grep -v 'plain' | head -n 1", returnStdout: true).trim()
                    env.LATEST_JAR = env.LATEST_JAR.replace("${OUTPUT_DIR}/", "")

                    if (!env.LATEST_JAR) {
                        error "No JAR file found in ${OUTPUT_DIR} directory!"
                    }
                    echo "Found JAR file: ${env.LATEST_JAR}"
                }
            }
        }

        stage('Transfer JAR to Remote Server') {
            steps {
                script {
                    echo "Transferring JAR file: ${env.LATEST_JAR}"
                    withCredentials([sshUserPrivateKey(credentialsId: "${SSH_CREDENTIALS_ID}", keyFileVariable: 'SSH_PRIVATE_KEY', usernameVariable: 'SSH_USER')]) {
                     sh """
                         scp -i \$SSH_PRIVATE_KEY -o StrictHostKeyChecking=no ${OUTPUT_DIR}/${LATEST_JAR} \$SSH_USER@\$SSH_SERVER:${REMOTE_DIR}
                     """
                    }
                }
            }
        }

        stage('Shutdown New JAR if run') {
            steps {
                script {
                     echo "Shutdown New JAR >>> shutdown New Jar "
                     shutdownJar(NEW_PROC_PORT, "new")
                }
            }
        }

        stage('Run New JAR on Remote Server') {
            steps {
                script {
                    echo "Run New JAR on Remote Server >>>>> ${NEW_PROC_PORT}"
                    withCredentials([sshUserPrivateKey(credentialsId: "${SSH_CREDENTIALS_ID}", keyFileVariable: 'SSH_PRIVATE_KEY', usernameVariable: 'SSH_USER')]) {
                        sh """
                        ssh -i \$SSH_PRIVATE_KEY -o StrictHostKeyChecking=no \$SSH_USER@${SSH_SERVER} '
                            echo "Starting the new JAR file on port ${NEW_PROC_PORT}..."
                            nohup java -Duser.timezone=Asia/Seoul -jar ${REMOTE_DIR}/${env.LATEST_JAR} --server.port=${NEW_PROC_PORT} --spring.profiles.active=${DEPLOY_PROFILE} > ${REMOTE_DIR}/${LOG_FILE_NAME}.log 2>&1 &
                            echo "New JAR file started on port ${NEW_PROC_PORT}."

                            # JAR 시작 로그 추적 (grep으로 패턴을 찾기)
                            echo "Waiting for the ${LOG_FILE_NAME}.log to contain startup message..."

                            # 로그에 성공 메시지나 실패 메시지가 있을 때까지 기다리기
                            while true; do
                                # "Started" 메시지가 로그에 있으면 성공 처리
                                if grep -q "${APP_START_MESSAGE}" ${REMOTE_DIR}/${LOG_FILE_NAME}.log; then
                                    echo "New JAR file started successfully."
                                    exit 0
                                # "Failed to start" 메시지가 로그에 있으면 실패 처리
                                elif grep -q "APPLICATION FAILED TO START" ${REMOTE_DIR}/${LOG_FILE_NAME}.log; then
                                    echo "New JAR file failed to start."
                                    exit 1
                                fi
                                sleep 2
                            done
                        '
                        """
                    }
                }
            }
        }

        stage('Shutdown Existing Application') {
            steps {
                script {
                     shutdownJar(EXSISTED_PROC_PORT, "existing")
                }
            }
        }

        stage('Run JAR on Existing Port') {
            steps {
                script {
                    withCredentials([sshUserPrivateKey(credentialsId: "${SSH_CREDENTIALS_ID}", keyFileVariable: 'SSH_PRIVATE_KEY', usernameVariable: 'SSH_USER')]) {
                        sh """
                        ssh -i \$SSH_PRIVATE_KEY -o StrictHostKeyChecking=no \$SSH_USER@${SSH_SERVER} '
                            echo "Starting the JAR file on existing port ${EXSISTED_PROC_PORT}..."
                            nohup java -Duser.timezone=Asia/Seoul -jar ${REMOTE_DIR}/${env.LATEST_JAR} --server.port=${EXSISTED_PROC_PORT} --spring.profiles.active=${DEPLOY_PROFILE} > ${REMOTE_DIR}/${LOG_FILE_NAME}.log 2>&1 &
                            echo "JAR file started on existing port ${EXSISTED_PROC_PORT}."

                            # JAR 시작 로그 추적 (grep으로 패턴을 찾기)
                            echo "Waiting for the ${LOG_FILE_NAME}.log to contain startup message..."

                            # 로그에 성공 메시지나 실패 메시지가 있을 때까지 기다리기
                            while true; do
                                # "Started" 메시지가 로그에 있으면 성공 처리
                                if grep -q "${APP_START_MESSAGE}" ${REMOTE_DIR}/${LOG_FILE_NAME}.log; then
                                    echo "New JAR file started successfully."
                                    exit 0
                                # "Failed to start" 메시지가 로그에 있으면 실패 처리
                                elif grep -q "APPLICATION FAILED TO START" ${REMOTE_DIR}/${LOG_FILE_NAME}.log; then
                                    echo "New JAR file failed to start."
                                    exit 1
                                fi
                                sleep 2
                            done
                        '
                        """
                    }
                }
            }
        }

        stage('Shutdown New JAR') {
            steps {
                script {
                    echo "Shutdown New JAR >>> shutdown New Jar "
                    shutdownJar(NEW_PROC_PORT, "new")
                }
            }
        }
    }



    post {
        always {
            echo 'Build finished'
        }
        success {
            echo 'Build succeeded'
        }
        failure {
            echo 'Build failed'
        }
    }
}
