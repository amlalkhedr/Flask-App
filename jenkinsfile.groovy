pipeline {
    agent any
    
    stages {
        stage('Get code from GitHub') {
            steps {
                git branch: 'main',
                    credentialsId: 'a842c327-2a5d-48bc-b984-66c548631238',
                    url: 'https://github.com/AhmedWael2397/Flask-App-Deployment-To-AWS`'
                sh 'cd DevOps Project'
            }
        }
        stage('Build Docker Image') {
            steps {
                sh 'docker build -t flaskapp:latest .'
            }
        }
        stage('Push Image to Nexus') {
            steps {
                script {
                    def nexusUser = 'admin'
                    def nexusPassword = 'admin'
                    def nexusUrl = 'http://adcfbfd6b687241449ee080f963abb58-591372858.eu-central-1.elb.amazonaws.com:8081/repository/Flask-app/'
                    def nexusHostname = 'adcfbfd6b687241449ee080f963abb58-591372858.eu-central-1.elb.amazonaws.com:8085'
                    def imageName = 'flaskapp:latest'

                    // Log in to Nexus
                    sh "echo ${nexusPassword} | docker login -u ${nexusUser} -p ${nexusPassword} ${nexusUrl}"

                    // Tag the image with the Nexus repository URL
                    sh "docker tag flaskapp:latest ${nexusHostname}/flaskapp:latest"

                    // Push the image to Nexus
                    sh "docker push ${nexusHostname}/flaskapp:latest"

                    // Log out from Nexus
                    sh "docker logout ${nexusHostname}"
                }
            }
        }
    }
}
