def call(String registryURL, String imageName) {
    script {
        sh """
        ssh -i ${env.SSH_KEY} -t ${env.SSH_USER}@${env.SSH_HOST} 'docker pull ${registryUrl}/${imageName}:latest'
        """
    }
}