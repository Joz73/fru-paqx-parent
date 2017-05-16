properties([
    pipelineTriggers([
      upstream(threshold: hudson.model.Result.SUCCESS, upstreamProjects: 'common-client-parent-master'),
      upstream(threshold: hudson.model.Result.SUCCESS, upstreamProjects: 'common-messaging-parent-master'),
      upstream(threshold: hudson.model.Result.SUCCESS, upstreamProjects: 'hdp-capability-registry-client-master'),
      upstream(threshold: hudson.model.Result.SUCCESS, upstreamProjects: 'symphony-root-parent-master'),
      upstream(threshold: hudson.model.Result.SUCCESS, upstreamProjects: 'virtualization-capabilities-api-master'),
      upstream(threshold: hudson.model.Result.SUCCESS, upstreamProjects: 'coprhd-adapter-parent-master'),
      upstream(threshold: hudson.model.Result.SUCCESS, upstreamProjects: 'rackhd-adapter-parent-master')
  ])
])
pipeline {    
    agent {
        node{
            label 'maven-builder'
            customWorkspace "workspace/${env.JOB_NAME}"
            }
    }
    environment {
        GITHUB_TOKEN = credentials('github-02')
        COMPOSE_PROJECT_NAME = "fru-paqx-parent-develop-${env.BUILD_NUMBER}"

    }
    options { 
        buildDiscarder(logRotator(artifactDaysToKeepStr: '30', artifactNumToKeepStr: '5', daysToKeepStr: '30', numToKeepStr: '5'))
        timestamps()
    }
    tools {
        maven 'linux-maven-3.3.9'
        jdk 'linux-jdk1.8.0_102'
    }
    stages {
        stage('Compile') {
            steps {
                sh "mvn -U clean install -DskipTest=true -DskipITs"
            }
        }
        stage('Prepare test services') {
            steps {
                sh "docker-compose -f ${WORKSPACE}/ci/docker/docker-compose.yml pull"

                sh "docker-compose -f ${WORKSPACE}/ci/docker/docker-compose.yml up --force-recreate -d"
            }
        }
        stage('Integration Test') {
            steps {
                sh "docker exec fru-paqx-test-${COMPOSE_PROJECT_NAME} mvn clean verify -DskipDocker=true"
            }
        }
	    stage('Package') {
	        when {
                expression {
                    return !(env.BRANCH_NAME ==~ /master|develop|release\/.*/)
                }
            }
		    steps {
                sh "mvn package -DskipTests=true -DskipITs"
            }
    	}
        stage('Artifact') {
            steps {
               sh "mvn install -DskipTests=true -DskipITs -P buildDockerImageOnJenkins"
               archiveArtifacts '**/*.rpm'
            }
        }

        stage('Deploy') {
	        when {
                expression {
                    return env.BRANCH_NAME ==~ /develop|release\/.*/
                }
            }
            steps {
                sh "mvn deploy -P buildDockerImageOnJenkins -DdockerImage.tag=api-gateway-parent-develop.${env.BUILD_NUMBER} -Ddocker.registry=docker-dev-local.art.local -DdeleteDockerImages=true -DskipTests=true -DskipITs"
		            archiveArtifacts artifacts: '**/*.rpm', fingerprint: true
            }
        }
        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') { 
                    doSonarAnalysis()    
                }
            }
        }
        stage('Third Party Audit') {
            steps {
                doThirdPartyAudit()
            }
        }
        stage('Github Release') {
	        when {
                expression {
                    return env.BRANCH_NAME ==~ /master|release\/.*/
                }
            }
            steps {
                githubRelease()
            }
        }
        stage('NexB Scan') {
	        when {
                expression {
                    return env.BRANCH_NAME ==~ /develop|release\/.*/
                }
            }
            steps {
                sh "mvn clean"
                doNexbScanning()
            }
        }
    }
    post {
        always{
            sh "docker-compose -f ${WORKSPACE}/ci/docker/docker-compose.yml down --rmi 'all' -v --remove-orphans"
            step([$class: 'WsCleanup'])   
        }
        success {


            emailext attachLog: true, 
                body: 'Pipeline job ${JOB_NAME} success. Build URL: ${BUILD_URL}', 
                recipientProviders: [[$class: 'CulpritsRecipientProvider']], 
                subject: 'SUCCESS: Jenkins Job- ${JOB_NAME} Build No- ${BUILD_NUMBER}', 
                to: 'pebuildrelease@vce.com'            
        }
        failure {
            emailext attachLog: true, 
                body: 'Pipeline job ${JOB_NAME} failed. Build URL: ${BUILD_URL}', 
                recipientProviders: [[$class: 'CulpritsRecipientProvider'], [$class: 'DevelopersRecipientProvider'], [$class: 'FailingTestSuspectsRecipientProvider'], [$class: 'UpstreamComitterRecipientProvider']], 
                subject: 'FAILED: Jenkins Job- ${JOB_NAME} Build No- ${BUILD_NUMBER}', 
                to: 'pebuildrelease@vce.com'
        }
    }
}
