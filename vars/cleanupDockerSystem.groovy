def call() {
    script {
        sh """
        ssh -i ${env.SSH_KEY} -t ${env.SSH_USER}@${env.STAGING_HOST} 'docker system prune -f'
        """
    }
}