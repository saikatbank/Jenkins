def call(String serviceName, String releaseRepo = "https://github.com/sproutsai-engg/release.git", String branch = "main", String credentialsId = "your-credentials-id") {
    // Pull the latest version repository using pullGitRepo
    pullGitRepo(releaseRepo, branch, credentialsId)

    // Read current version
    def jsonText = readFile("release/release-versions.json")
    def jsonData = new groovy.json.JsonSlurper().parseText(jsonText)

    def currentRelease = jsonData.release
    def deployCount = jsonData.services[serviceName] ?: 0

    // Increment deploy count
    def newDeployCount = deployCount.toInteger() + 1

    // Update JSON file using `jq`
    sh """
        jq --arg SERVICE "${serviceName}" --argjson COUNT ${newDeployCount} \
        '.services[\$SERVICE] = \$COUNT' release/release-versions.json \
        > release/tmp_versions.json && mv release/tmp_versions.json release/release-versions.json
    """

    // Commit and push the updated version
    sh """
        cd release
        git config user.name "dev"
        git config user.email "dev@sproutsai.com"
        git add release-versions.json
        git commit -m "Updated ${serviceName} deployment count to ${newDeployCount}"
        git push origin ${branch}
    """

    // Generate the new image tag
    env.NEW_TAG = "${currentRelease}.${newDeployCount}"
    echo "New image tag: ${env.NEW_TAG}"
}
