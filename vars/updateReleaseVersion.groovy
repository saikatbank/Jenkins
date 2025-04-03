def call(String serviceName, String releaseRepo = "https://github.com/sproutsai-engg/release.git", String branch = "main", String credentialsId = "GitPullCredential") {
    // Pull the latest version repository using pullGitRepo
    pullGitRepo(releaseRepo, branch, credentialsId)

    // Ensure we are on the correct branch
    sh "git checkout ${branch}"

    // Read current version using readJSON
    def jsonData = readJSON file: "release-versions.json"

    def currentRelease = jsonData.release
    def deployCount = jsonData.services.get(serviceName, 0)

    // Increment deploy count
    def newDeployCount = deployCount.toInteger() + 1
    jsonData.services[serviceName] = newDeployCount

    // Write the updated JSON back to file
    writeJSON file: "release-versions.json", json: jsonData, pretty: 4

 // Set up Git authentication for push
    withCredentials([usernamePassword(credentialsId: credentialsId, passwordVariable: 'GIT_PASSWORD', usernameVariable: 'GIT_USERNAME')]) {
        sh """
            git config user.name "dev"
            git config user.email "dev@sproutsai.com"
            git remote set-url origin https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/sproutsai-engg/release.git
            git add release-versions.json
            git commit -m "Updated ${serviceName} deployment count to ${newDeployCount}"
            git push origin ${branch}
        """
    }

    // Generate the new image tag
    env.NEW_TAG = "${currentRelease}.${newDeployCount}"
    echo "New image tag: ${env.NEW_TAG}"
}
