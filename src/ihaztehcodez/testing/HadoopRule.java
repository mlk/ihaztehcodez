package ihaztehcodez.testing;

import org.apache.hadoop.fs.FileContext;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.hdfs.HdfsConfiguration;
import org.apache.hadoop.hdfs.MiniDFSCluster;
import org.junit.rules.ExternalResource;

public class HadoopRule extends ExternalResource {
    private HdfsConfiguration conf = new HdfsConfiguration();
    private MiniDFSCluster cluster;

    private FileSystem mfs;
    private FileContext mfc;


    @Override
    protected void before() throws Throwable {
        cluster = new MiniDFSCluster.Builder(conf).numDataNodes(3).build();
        cluster.waitActive();
        mfs = cluster.getFileSystem();
        mfc = FileContext.getFileContext();
    };

    @Override
    protected void after() {
        cluster.shutdown(true);
    }

    public FileSystem getMfs() {
        return mfs;
    }

    public int getNameNodePort() {
        return cluster.getNameNodePort();
    }
}
