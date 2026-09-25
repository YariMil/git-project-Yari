# git-project-Yari

The init() method creates the git directory, the git/objects directory, the git/index file, and the git/HEAD file. If they all already exist, it reports that the repository already exists. If not, it reports that the Git repository was created.

# hashFile
hashFile takes in a String parameter representing the file name and uses the Files class to check whether the file path is a valid file path. It then reads the contents of the file into an array of bytes and then uses the SHA-1 algorithm to hash the file contents. It then
reformats it into a hex string.