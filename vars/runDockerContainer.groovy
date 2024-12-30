def call(String containerName, String portMapping, String environmentVariable, String registryUrl, String imageName) {
    script {
        sh """
        ssh -i ${env.SSH_KEY} -t ${env.SSH_USER}@${env.SSH_HOST} '
        docker run -d -p ${portMapping} -e "ENVIRONMENT_VARIABLE_VALUE=${environmentVariable}" --restart=always --name=${containerName} ${registryUrl}/${imageName}:latest
        '
        """
    }
}