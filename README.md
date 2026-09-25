# git-project-Yari

The init() method creates the git directory, the git/objects directory, the git/index file, and the git/HEAD file. If they all already exist, it reports that the repository already exists. If not, it reports that the Git repository was created.

# hashFile
hashFile takes in a String parameter representing the file path and uses the Files class to check whether the file path is a valid file path. It then reads the contents of the file into an array of bytes and then uses the SHA-1 algorithm to hash the file contents. It then
reformats it into a hex string.

# createBlob
createBlob takes in a String parameter representing the file path. It hashes the file uses the hashFile method and saves the hash into a String. It then reads the contents of the file into an array of bytes and then creates a new file in git/objects with the name of the hash. It writes the bytes of the hashed file into this new file.