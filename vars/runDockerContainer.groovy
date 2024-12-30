def call(String containerName, String portMapping, String environmentVariable, String registryUrl, String imageName) {
    script {
        def imageURL = "${registryUrl}/${imageName}:latest"
        sh """
        ssh -i ${env.SSH_KEY} -t ${env.SSH_USER}@${env.SSH_HOST} '
        docker run -d -p ${portMapping} -e "ENVIRONMENT_VARIABLE_VALUE=${environmentVariable}" --restart=always --name=${containerName} ${imageURL}
        '
        """
    }
}