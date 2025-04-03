def call(String serviceName, String releaseRepo = "https://github.com/sproutsai-engg/release.git", String branch = "main", String credentialsId = "GitPullCredential") {
    // Pull the latest code
    pullGitRepo(releaseRepo, branch, credentialsId)

    // Read current version using readJSON
    def jsonData = readJSON file: "release-versions.json"

    def currentRelease = jsonData.release
    def deployCount = jsonData.services.get(serviceName, 0)

    // Increment deploy count
    def newDeployCount = deployCount.toInteger() + 1
    jsonData.services[serviceName] = newDeployCount

    // Write the updated JSON back to file
    writeJSON file: "release-versions.json", json: jsonData, pretty: 4

    // Push updated file to GitHub
    withCredentials([usernamePassword(credentialsId: credentialsId, passwordVariable: 'GIT_PASSWORD', usernameVariable: 'GIT_USERNAME')]) {
        sh """
            git config user.name "dev"
            git config user.email "dev@sproutsai.com"
            git remote set-url origin https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/sproutsai-engg/release.git
            
            # Stash changes before pulling
            git stash
            
            # Fetch and rebase to ensure latest changes
            git pull --rebase origin ${branch}

            # Apply stashed changes
            git stash pop || echo "No changes to apply from stash"

            git add release-versions.json
            git commit -m "Updated ${serviceName} deployment count to ${newDeployCount}"

            # Force push to resolve non-fast-forward errors
            git push --force-with-lease origin ${branch}
        """
    }

    // Generate the new image tag
    env.NEW_TAG = "${currentRelease}.${newDeployCount}"
    echo "New image tag: ${env.NEW_TAG}"
}
