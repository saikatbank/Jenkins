def call(String containerName) {
    script {
        sh """
        ssh -i ${env.SSH_KEY} -t ${env.SSH_USER}@${env.SSH_HOST} '
        container_id=\$(docker ps -aqf "name=${containerName}")
        if [ -n "\$container_id" ]; then
            docker stop \$container_id
            docker rm -f \$container_id
        fi
        '
        """
    }
}