pipeline {
    agent any

    tools {
        maven "maven3.9.9"
        jdk "jdk17"
    }

    stages {

        stage('Checkout Automation Code') {
            steps {
                git branch: 'master', url: 'https://github.com/Chinmay2305/AerFin.git'
            }
        }

        stage('Build & Run Tests') {
            steps {
                bat 'mvn clean test -DskipTests=false'
            }
        }

        stage('Archive Extent Report') {
            steps {
                archiveArtifacts artifacts: 'Reports/*.html', fingerprint: true
            }
        }

        stage('Publish Report in Jenkins') {
            steps {
                publishHTML(target: [
                    reportName : 'Extent_Report',
                    reportDir  : 'Reports',
                    reportFiles: '*.html',
                    keepAll    : true,
                    alwaysLinkToLastBuild: true,
                    allowMissing: false
                ])
            }
        }

        stage('Print Clickable Report Link') {
            steps {
                script {
                    // Get the BUILD_URL provided by Jenkins
                    def buildUrl = env.BUILD_URL

                    // Find all HTML reports in Reports/ directory
                    def reports = findFiles(glob: 'Reports/*.html')

                    if (reports.length > 0) {
                        // Sort by name or last modified; here we pick the last one
                        def latestReport = reports[-1].name
                        
                        // Print clickable link in console
                        echo "Click to open ExtentReport: ${buildUrl}artifact/Reports/${latestReport}"
                    } else {
                        echo "No report found in Reports/ folder!"
                    }
                }
            }
        }
    }

    post {
        always {
            echo "Build completed"
        }
    }
}