pipeline {
    agent any
    
    stages {
        stage('Load Configuration') {
            steps {
                script {
                    def configs = readJSON(file: '/home/devopsuser/vm_configs.json')
                    env.SSH_KEY = configs.production.SSH_KEY
                    env.SSH_USER = configs.production.SSH_USER
                    env.SSH_HOST = configs.production.SSH_HOST
                    env.NODE_ENV = configs.production.NODE_ENV
                    env.ELASTICSEARCH_USERNAME = configs.production.ELASTICSEARCH_USERNAME
                    env.ELASTICSEARCH_PASSWORD = configs.production.ELASTICSEARCH_PASSWORD
                }
            }
        }
        stage('Print Configuration') {
            steps {
                script {
                    echo "Loaded Configuration:"
                    echo "SSH_KEY: ${env.SSH_KEY}"
                    echo "SSH_USER: ${env.SSH_USER}"
                    echo "SSH_HOST: ${env.SSH_HOST}"
                    echo "NODE_ENV: ${env.NODE_ENV}"
                    echo "ELASTICSEARCH_USERNAME: ${env.ELASTICSEARCH_USERNAME}"
                    echo "ELASTICSEARCH_PASSWORD: ${env.ELASTICSEARCH_PASSWORD}"
                }
            }
        }
    }
}