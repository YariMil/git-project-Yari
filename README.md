### git-project-Yari

The init() method creates the git directory, the git/objects directory, the git/index file, and the git/HEAD file. It also creates git/refs/heads/main and writes "ref: refs/heads/main" into HEAD. If they all already exist, it reports that the repository already exists. If not, it reports that the Git repository was created.

# hashFile

hashFile takes in a String parameter representing the file path and uses the Files class to check whether the file path is a valid file path. It then reads the contents of the file into an array of bytes and then uses the SHA-1 algorithm to hash the file contents. It then
reformats it into a hex string.

# createBlob

createBlob takes in a String parameter representing the file path. It hashes the file uses the hashFile method and saves the hash into a String. It then reads the contents of the file into an array of bytes and then creates a new file in git/objects with the name of the hash. It writes the bytes of the hashed file into this new file.

# stageFiles

stageFiles takes in a String array representing the file paths of the files staged and a String array with their hashes. It creates BLOBS from the files using createBlob. It then writes the hashes next to the name of the file they represent in git/index. If any file path is invalid, or if a hash for a file isn't provided, it throws an exception.

# Testing methodology

Testing begins by first initializing the git repository, then checking if all required files and directory were created using checkIfGitExists. The init() method is called again to make sure it doesn't recreate the repositories (done by creating wow.txt in git/objects during the initial set up and checking if it exists after the second set up). The cleanup method is used at the end to delete the git structure and test files. test.txt is then hashed for testing purposes (hash is checked using the terminal). From there, stageFiles is tested twice. Both times the hashes of the files are printed out. The contents of the index file is also printed out both times for verification of the hashes.

## GP-5 Written by Yari

# Bugs found after reforking

Fixed all commits having an extra new line by removing the extra new line addition in commit(). Also fixed index always having a new line by removing the extra new line addition in both restoreHistory() and stageFiles().

# checkout(String input)

Checks out either a branch or a detached commit. It first goes through all the branches created and checks if the input is a branch name. If it is, it calls checkoutBranch. If it isn't a branch, it goes through the git/objects folder and checks to see if the input is a commit hash. If it is, it calls checkoutCommit. If it is neither, it creates a new branch using createBranch and switches to it using checkoutBranch.

Use examples:
checkout("feature") --> Switches to a new branch called "feature"
checkout("dc0da3e895478c37ae3f68c68bbf15b8bd762691") -> creates a detached head with the dc0da3e895478c37ae3f68c68bbf15b8bd762691 commit.

# createBranch(String newBranch)

Creates a new branch by writing a new file in refs/heads with the name of the branch. Writes in the hash of the current commit of the current branch into the new branch file.

# checkoutBranch(String branch)

Checks out a branch by writing "ref: refs/heads/branch" into the HEAD file and then calling restoreHistory() using the commit on the new branch.

# deleteLastNewLine()

Goes through the index and deletes the last new line to prevent it from having an unncessary new line.

# restoreDirectory(String dirPath, String hash)

Restores a directory from the working history by taking its hash and the directory it is in (empty string if root directory). Reads the git/objects file corresponding to the directory and restores every BLOB file in it using restoreFile. Every time it encounters a tree entry in the git/objects, it calls itself on the new directory, passing the tree hash and an updated dirPath with the name of the current directory added to it. When restoring a file, the index is updated to reflect the addition (it is being rewritten).

# restoreFile(String path, String fileHash)

Restores a BLOB file from the working history by taking its hash and its file path. The entire method is in a try catch block that prints an error message if the file doesn't exist. It then reads the BLOB file in git/objects corresponding to the hash, takes all the bytes from that file, and writes them into a new created file with the String path.

# restoreHistory(String commit)

Takes in a String representing a commit hash, accesses the git/objects file corresponding to the hash, and reads in the root tree hash from that commit. Recreates an empty git/index file, and then calls restoreDirectory using the root hash and an empty string and then uses deleteLastNewLine() to delete the last new line from the restored index file. Then calls checkRepository() on the recreated root repository.

# checkRepository(String dirPath, String indexContent, String oldContent)

Goes through a repository and checks each file in the repository to see if it was present in the old history that was present before restoreHistory() was called. If it is, it checks the new indexContent to see if it is still staged in the new version. If it isn't the file is deleted. Unstaged files are not checked. If the method encounters a subdirectory it calls itself on the subdirectory.

# getIndexContent()

Goes through the git/index file and compiles its contents into a string.

# compileWorkingList(ArrayList<String> workingList)

Consolidates a working list into a single string.

# getBranchAndHash()

Reads the head file for the current branch, and then reads the corresponding branch file for the current commit hash on that branch. Returns a single string wiht the current branch and the corresponding branch file.

# getCurrentCommitHash()

Gets the current commit hash using getBranchAndHash() and taking only the commit hash part.

# getCurrBranch()

Gets the current branch using getBranchAndHash() and taking only the branch part.
