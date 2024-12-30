pipeline {
    agent any

    parameters {
        choice(
            name: 'ENVIRONMENT',
            choices: ['production', 'tailorbird', 'suzy'],
            description: 'Select the environment to load configuration for.'
        )
    }

    stages {
        stage('Load Configuration') {
            steps {
                script {
                    def configs = readJSON(file: '/home/devopsuser/vm_configs.json')
                    def selectedEnv = params.ENVIRONMENT
                    env.SSH_KEY = configs["${selectedEnv}"].SSH_KEY
                    env.SSH_USER = configs["${selectedEnv}"].SSH_USER
                    env.SSH_HOST = configs["${selectedEnv}"].SSH_HOST
                    env.NODE_ENV = configs["${selectedEnv}"].NODE_ENV
                    env.ELASTICSEARCH_USERNAME = configs["${selectedEnv}"].ELASTICSEARCH_USERNAME
                    env.ELASTICSEARCH_PASSWORD = configs["${selectedEnv}"].ELASTICSEARCH_PASSWORD
                }
            }
        }
        stage('Print Configuration') {
            steps {
                script {
                    echo "Loaded Configuration for ${params.ENVIRONMENT}:"
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
