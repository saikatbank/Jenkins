def call(String serviceName, String releaseRepo = "https://github.com/sproutsai-engg/release.git") {
    pipeline {
        agent any

        environment {
            RELEASE_VERSION_FILE = "release-versions.json"
            TMP_FILE = "tmp_versions.json"
        }

        stages {
            stage('Clone Version Repository') {
                steps {
                    script {
                        sh "rm -rf release-versioning || true"
                        sh "git clone ${releaseRepo} release"
                    }
                }
            }

            stage('Read Current Version') {
                steps {
                    script {
                        def jsonText = readFile("release/${RELEASE_VERSION_FILE}")
                        def jsonData = new groovy.json.JsonSlurper().parseText(jsonText)

                        env.CURRENT_RELEASE = jsonData.release
                        env.DEPLOY_COUNT = jsonData.services[serviceName] ?: 0
                    }
                }
            }

            stage('Increment Version') {
                steps {
                    script {
                        env.NEW_DEPLOY_COUNT = (env.DEPLOY_COUNT.toInteger() + 1).toString()
                        def updatedJson = sh(
                            script: """jq --arg SERVICE "${serviceName}" --argjson COUNT ${env.NEW_DEPLOY_COUNT} \
                                '.services[\$SERVICE] = \$COUNT' release/${RELEASE_VERSION_FILE} \
                                > release/${TMP_FILE} && mv release/${TMP_FILE} release/${RELEASE_VERSION_FILE}""",
                            returnStdout: true
                        )
                    }
                }
            }

            stage('Commit & Push Updated Version') {
                steps {
                    script {
                        sh """
                            cd release
                            git config user.name "dev"
                            git config user.email "dev@sproutsai.com"
                            git add ${RELEASE_VERSION_FILE}
                            git commit -m "Updated ${serviceName} deployment count to ${env.NEW_DEPLOY_COUNT}"
                            git push origin main
                        """
                    }
                }
            }

            stage('Generate Image Tag') {
                steps {
                    script {
                        env.NEW_TAG = "${env.CURRENT_RELEASE}.${env.NEW_DEPLOY_COUNT}"
                        echo "New image tag: ${env.NEW_TAG}"
                    }
                }
            }
        }
    }
}
