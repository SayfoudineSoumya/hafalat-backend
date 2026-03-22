pipeline {
    agent any
    
    stages {

        // stage('Checkout') {
        //     steps {
        //          git 'https://github.com/SayfoudineSoumya/hafalat-backend.git'
        //     }
        // }

        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh 'mvn sonar:sonar -Dsonar.projectKey=hafalat-backend'
                }
            }
        }

        stage('Docker Build') {
            steps {
                sh 'docker build -t hafalat-backend:latest .'
            }
        }

        stage('Deploy') {
            steps {
                sh 'docker compose -f ../hafalat-deployment/docker-compose.yml up -d --build'
            }
        }
    }

    post {
        success {
            echo '✅ Build Success'
        }
        failure {
            echo '❌ Build Failed'
        }
    }
}
