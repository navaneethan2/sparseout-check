pipeline{
      agent any
  
  stages {
    stage('check change') {
      when {
         changeset "UI/Module_1"
      }
      steps{
          sh "echo I will build now"
      }
    
    }
  
  }

}
