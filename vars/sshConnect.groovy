def call(String sshKey, String sshUser, String sshHost) {
    // Settings up SSH connection details as environment variables
    env.SSH_KEY = sshKey
    env.SSH_USER = sshUser
    env.SSH_HOST = sshHost
}