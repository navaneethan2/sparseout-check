pipeline{
      agent any
  
  stages {
    stage(''check change) {
      when {
         changeset "sparseout-check"
      }
      steps{
          sh "echo I will build now"
      }
    
    }
  
  }

}
