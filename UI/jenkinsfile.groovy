pipeline{
      agent any
  
  stages {
    stage('check change') {
      when {
         changeset "UI"
      }
      steps{
          sh "echo I will build now"
      }
    
    }
  
  }

}
