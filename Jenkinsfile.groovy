

 pipeline {

  agent any

     parameters {
         choice(name: 'env', choices: ['ci'], description: 'To load the parameters in the jenkinfile')
        choice(name: 'App_Module_Name', choices: ['Module_1', 'Module_2', 'Module_3', 'Module_4', 'Module_5'], description: 'The number of parallel customer and payment regression packs to run.')
        booleanParam(name: 'Send_Email', description: 'Send Email',  defaultValue: true)
        text(name: 'Email_To',defaultValue: 'navaneethan.chinnasamy@rci.com',description: 'Email recipients')
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

        stage("App Name"){
           steps{
             sh 'echo $App_Module_Name'
           }
        }

        stage("sparse-checkout") {
           steps {
              checkout([$class: 'GitSCM', branches: [[name: '*/master']], extensions: [[$class: 'SparseCheckoutPaths', sparseCheckoutPaths: [[path: "${App_Module_Name}"]]]], userRemoteConfigs: [[url: 'https://github.com/navaneethan2/sparseout-check.git']]])
           }
        }

         stage("package") {
            steps{
               sh 'cd $App_Module_Name && \
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


