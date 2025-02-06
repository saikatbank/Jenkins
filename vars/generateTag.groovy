def call() {
    script {
        def tag = "1.0.1" //Default tag
        if (fileExists('tag.txt')) {
            def lastTag = readFile('tag.txt').trim()
            def parts = lastTag.tokenize('.')
            def newPatch = (parts[-1] as Integer) + 1
            tag = "${parts[0]}.${parts[1]}.${newPatch}"
        }
        writeFile file: 'tag.txt', text: tag  //Write the new tag to file

        echo "Generated tag: ${tag}"
        return tag
    }
}