def call(String registryURL, String imageName) {
    script {
        def imageURL = "${registryURL}/${imageName}:latest"
        sh """
        ssh -i ${env.SSH_KEY} -t ${env.SSH_USER}@${env.SSH_HOST} 'docker pull ${imageURL}'
        """
    }
}