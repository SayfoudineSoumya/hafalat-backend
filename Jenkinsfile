pipeline {
    agent {
        docker {
            image 'my-maven-git:latest'
            args '-v /var/run/docker.sock:/var/run/docker.sock -v /var/jenkins_home/.m2:/root/.m2 --network hafalat-devops_hafalat-network'
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
            agent any
            steps {
                sh '''
                    docker pull $IMAGE_NAME:latest
                    docker stop hafalat-backend || true
                    docker rm hafalat-backend || true
                    docker run -d \
                      --name hafalat-backend \
                      --network hafalat-devops_hafalat-network \
                      -p 8080:8080 \
                      -e SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/hafalat \
                      -e SPRING_DATASOURCE_USERNAME=admin \
                      -e SPRING_DATASOURCE_PASSWORD=admin \
                      $IMAGE_NAME:latest
                '''
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
