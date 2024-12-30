def call(String imageUrl) {
    script {
        sh """
        ssh -i ${env.SSH_KEY} -t ${env.SSH_USER}@${env.STAGING_HOST} 'docker pull ${imageUrl}'
        """
    }
}