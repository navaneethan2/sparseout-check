def getAllRepos(String repoFile) {

    return sh(script: "cat ${repoFile} | jq -r .'repositories.sublists[] | .repos[].name'", returnStdout: true)
}

def createTextFileFromRepoList(String outputFileName, String repoList) {

    def reposArray = repoList.split()

    reposArray.each {
        def gitRepoName = "${it}"
        sh "echo ${gitRepoName} >> ${outputFileName}.txt"
    }
}

return this;
