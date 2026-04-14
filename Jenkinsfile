pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                sh 'mvn clean compile -B'
            }
        }

        stage('Test') {
            parallel {
                stage('Backend Tests') {
                    steps {
                        sh 'mvn test -B'
                    }
                    post {
                        always {
                            junit allowEmptyResults: true,
                                 testResults: '**/target/surefire-reports/*.xml'
                        }
                    }
                }
                stage('Frontend Lint') {
                    steps {
                        dir('frontend') {
                            sh 'npm install'
                            sh 'npm run lint'
                        }
                    }
                }
            }
        }

        stage('Package') {
            steps {
                sh 'mvn package -DskipTests -B'
            }
            post {
                success {
                    archiveArtifacts artifacts: '**/target/*.jar',
                                     fingerprint: true
                }
            }
        }

}

    post {
        success {
            echo 'Pipeline completata con successo!'
        }
        failure {
            echo 'Pipeline fallita. Controlla i log per i dettagli.'
        }
    }
}
