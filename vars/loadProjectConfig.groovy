import groovy.json.JsonSlurper

def call(String key) {
    // Path to the JSON file
    def jsonFilePath = '/home/devopsuser/testProjectConfig.json' // Replace with actual path
    
    // Read the JSON file
    def jsonFile = new File(jsonFilePath)
    if (!jsonFile.exists()) {
        error "JSON file not found at ${jsonFilePath}"
    }

    // Parse the JSON content
    def jsonContent = new JsonSlurper().parse(jsonFile)

    // Retrieve the value corresponding to the key
    def value = jsonContent[key]
    if (!value) {
        error "Key '${key}' not found in the JSON configuration"
    }

    return value
}
