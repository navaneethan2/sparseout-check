#!/usr/bin/env groovy

// see https://jenkins.io/doc/book/pipeline/syntax/#declarative-pipeline for syntax help

 pipeline {

  agent any

     triggers {
         // Set to run on Mon, Tue, Wed, Thurs, Fri
          cron 'H 17 * * 1-5'
     }

     parameters {
         choice(name: 'env', choices: ['ci', 'Dev'], description: 'To load the parameters in the jenkinfile.')
         booleanParam(name: 'Build_All', description: 'Build All', defaultValue: false)
         string(name: 'Branch_To_Build', defaultValue: 'master', description: 'Build_All: Branch to build i.e. master/release-brYY.MM')
         booleanParam(name: 'Send_Email', description: 'Send Email',  defaultValue: true)
         text(name: 'Email_To',defaultValue: 'navaneethan.chinnasamy@gmail.com',description: 'Email recipients')
     }


     stages {

         stage('CI') {
             steps {
                 script {
                     echo 'CI Stage'
                     // Stop the CI Build immediately. No need to accidentally trigger an acceptance test for a CI build.
                     if (params.env == 'ci') {
                         def jobname = env.JOB_NAME
                         def buildnum = env.BUILD_NUMBER.toInteger()

                         def job = Jenkins.instance.getItemByFullName(jobname)
                         for (build in job.builds) {
                             print build
                             if (!build.isBuilding()) {
                                 continue;
                             }
                             if (buildnum == build.getNumber().toInteger()) {
                                 println "Stop this running CI build. No need to run tests for a CI build."
                                 build.doStop();
                             }
                         }
                     }
                 }
             }
         }
         stage("commits") {
             steps {
                 script{
                    def repoFile = "modules.json"
                    def repos = modules.getAllRepos(repoFile)

                    txtFileName = "repos-to-check"
                    repo.createTextFileFromRepoList(txtFileName, repos)

                     sh "cat repos-to-check"

                    /*withCredentials([string(credentialsId: 'tfs_user', variable: 'API_Token')]) {
                        sh """
                            chmod +x svc-commits-last-night-by-branch.sh
                            ./svc-commits-last-night-by-branch.sh ${API_Token} ${Branch_To_Check} ${txtFileName}.txt
                        """*/
                    }

                }
            }
         }
        stage("App Name"){
           steps{
             sh 'echo $App_Module_Name'
           }
        }

        stage("sparse-checkout") {
           steps {
              checkout([$class: 'GitSCM', branches: [[name: '*/master']], extensions: [[$class: 'SparseCheckoutPaths', sparseCheckoutPaths: [[path: "UI/${App_Module_Name}"]]]], userRemoteConfigs: [[url: 'https://github.com/navaneethan2/sparseout-check.git']]])
           }
        }

         stage("package") {
            steps{
               sh 'cd UI/$App_Module_Name && \
                   echo $PWD && \
                   echo "mvn -f pom.xml sonar:sonar clean install"'
            }
         }

     }


  post {
      always {
          echo 'Cleaning workspace'
          cleanWs() /* clean up our workspace */
      }
  }

 }


