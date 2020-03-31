pipeline {
	agent any

	environment {
		KEYSTORE = credentials('software-quality-tools')
		UPDATE_SITE_URL = "sftp://repo@repo.se.internal/var/www/html/se"
	}

	stages {
		stage('Build') {
			steps {
				sh 'mvn --batch-mode compile'
			}
		}
		stage('Test') {
			steps {
				sh 'mvn --batch-mode test'
			}
		}
		stage('Check') {
			steps {
				sh 'mvn -Dtycho.mode=maven --batch-mode package checkstyle:checkstyle pmd:pmd -Dworkspace=' + env.WORKSPACE // spotbugs:spotbugs
			}
			post {
				always {
					recordIssues enabledForFailure: true, tools: [mavenConsole(), java(), javaDoc()]
					recordIssues enabledForFailure: true, tool: checkStyle()
//					recordIssues enabledForFailure: true, tool: spotBugs()
					recordIssues enabledForFailure: true, tool: pmdParser()
				}
			}
		}
		stage('Package') {
			steps {
				sh 'mvn --batch-mode package'
			}
		}
		stage ('Update Repository') {
			when {
				branch 'master'
			}
			steps {
				sh 'mvn --settings settings.xml --batch-mode install -Dkeystore=${KEYSTORE} -Dupdate-site-url=${UPDATE_SITE_URL}'
			}
		}
	}
}
