pipeline {
    agent {
        docker {
            image 'my-maven-git:latest'
            args '-v /var/run/docker.sock:/var/run/docker.sock -v /var/jenkins_home/.m2:/root/.m2'
        }
    }

    environment {
        IMAGE_NAME = 'soumayasayfoudine/hafalat-backend'
        VERSION = "1.0.${BUILD_NUMBER}"
    }

    stages {

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
                sh 'docker build -t $IMAGE_NAME:$VERSION .'
                sh 'docker tag $IMAGE_NAME:$VERSION $IMAGE_NAME:latest'
            }
        }

        stage('Docker Push') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'DockerHubjenkinsCI', usernameVariable: 'USER', passwordVariable: 'PASS')]) {
                    sh 'echo $PASS | docker login -u $USER --password-stdin'
                    sh 'docker push $IMAGE_NAME:$VERSION'
                    sh 'docker push $IMAGE_NAME:latest'
                }
            }
        }

        stage('Deploy') {
            steps {
                sh 'docker-compose -f ../hafalat-deployment/docker-compose.yml pull'
                sh 'docker-compose -f ../hafalat-deployment/docker-compose.yml up -d'
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
