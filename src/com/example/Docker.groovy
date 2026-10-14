#!/user/bin/env groovy
package com.example

class Docker implements Serializable {

    def script

    Docker(script) {
        this.script = script
    }

    def buildDockerImage(String imageName) {
        script.echo "building the docker image..."
        script.sh "docker build -t $imageName ."
        }

    def dockerLogin() {
        script.echo "logging into Docker Hub..."

        script.withCredentials([
            script.usernamePassword(
                credentialsId: 'docker-hub-repo',
                usernameVariable: 'DOCKER_USER',
                passwordVariable: 'DOCKER_TOKEN'
            )
        ]) {
            script.sh '''
                set +x
                printf '%s' "$DOCKER_TOKEN" |
                    docker login -u "$DOCKER_USER" --password-stdin
            '''
            }
    }

    def dockerPush(String imageName) {
        script.echo "pushing the docker image..."
        script.sh "docker push $imageName"
    }
}