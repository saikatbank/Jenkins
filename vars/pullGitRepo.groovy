def call(String repoUrl, String branch, String credentialsId) {
    script {
        echo "Pulling the latest code from ${repoUrl} branch ${branch}"
        checkout([$class: 'GitSCM', 
        branches: [[name: '*/${branch}']],
        userRemoteConfigs: [[url: "${repoUrl}", credentialsId: "${credentialsId}"]]])

        echo "Code pulled successfully"
    }
}