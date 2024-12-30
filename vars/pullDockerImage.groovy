def call(String imageUrl) {
    script {
        sh """
        ssh -i ${env.SSH_KEY} -t ${env.SSH_USER}@${env.SSH_HOST} 'docker pull ${imageUrl}'
        """
    }
}