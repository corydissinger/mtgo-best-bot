node {
    def server
    def buildInfo
    def rtMaven

    stage ('Build') {
        git branch: 'master', credentialsId: '44716ae8-58f8-43cf-88e0-9c9303e7c7fa', url: 'https://github.com/corydissinger/mtgo-best-bot.git'
    }

    stage ('Artifactory configuration') {
        server = Artifactory.server localhost

        rtMaven = Artifactory.newMavenBuild()
        rtMaven.tool = autoinstall // Tool name from Jenkins configuration
        rtMaven.deployer releaseRepo: 'libs-release-local', snapshotRepo: 'libs-snapshot-local', server: server
        rtMaven.resolver releaseRepo: 'libs-release', snapshotRepo: 'libs-snapshot', server: server
        rtMaven.deployer.deployArtifacts = false // Disable artifacts deployment during Maven run

        buildInfo = Artifactory.newBuildInfo()
    }

    stage ('Test') {
        rtMaven.run pom: 'mtgo-best-bot/pom.xml', goals: 'clean test'
    }

    stage ('Install') {
        rtMaven.run pom: 'mtgo-best-bot/pom.xml', goals: 'install', buildInfo: buildInfo
    }

    stage ('Deploy') {
        rtMaven.deployer.deployArtifacts buildInfo
    }

    stage ('Publish build info') {
        server.publishBuildInfo buildInfo
    }
}