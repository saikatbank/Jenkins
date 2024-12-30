def call(String imageName, String registryUrl, String tag) {
    script {
        sh """
        docker build -t ${registryUrl}/${imageName}:${tag} .
        docker tag ${registryUrl}/${imageName}:${tag} ${registryUrl}/${imageName}:latest
        """

        echo "Docker image built and tagged as ${registryUrl}/${imageName}:${tag} and ${registryUrl}/${imageName}:latest"

        sh "docker push ${registryUrl}/${imageName}:${tag}"
        sh "docker push ${registryUrl}/${imageName}:latest"
    }
}
