

 pipeline {

  agent any

     parameters {

        choice(name: 'App_Module_Name', choices: ['Module_1', 'Module_2', 'Module_3', 'Module_4'], description: 'The number of parallel customer and payment regression packs to run.')

     }


     stages {

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


     } }


