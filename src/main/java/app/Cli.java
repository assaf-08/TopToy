package app;
import blockchain.Blockchain;
import blockchain.data.BCS;
import com.google.protobuf.ByteString;
import config.Config;
//import crypto.rmfDigSig;
import crypto.DigestMethod;
import crypto.blockDigSig;
import das.ms.BFD;
import org.apache.commons.cli.*;
import org.apache.commons.lang.ArrayUtils;
import proto.Types;
import utils.statistics.Statistics;
import utils.CSVUtils;

import javax.print.DocFlavor;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static crypto.blockDigSig.hashBlockData;
import static java.lang.Math.min;
import static java.lang.StrictMath.max;
import static java.lang.String.format;
import static java.lang.String.valueOf;
import static utils.statistics.Statistics.*;

public class Cli {
    private final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Cli.class);
        private Options options = new Options();
        private long totalRT = 0;
        static String outPath = "/tmp/JToy/res/";
        AtomicBoolean recorded = new AtomicBoolean(false);
        public Cli() {
            options.addOption("help", "print this message");
            options.addOption("init", "init the server");
            options.addOption("serve", "start the server\n" +
                    "Note that before lunching the command, all other servers has to be initialized first");
            options.addOption("stop", "terminate the server");
            options.addOption("quit", "exit Toy terminal");
            options.addOption(Option.builder("tx")
                            .hasArg()
                            .desc("-a: add new transaction\n" +
                                    "Usage: tx -a [data]\n" +
                                    "-s: check the status of a transaction\n" +
                                    "Usage: tx -s [txID]")
                            .build()); // TODO: How?
            options.addOption(Option.builder("wait")
                    .hasArg()
                    .desc("Usage: wait [sec]\n" +
                            "waits for [sec] second")
                    .build());
            options.addOption(Option.builder("res")
                    .hasArg()
                    .desc("Write the results into csv file\n" +
                            "Usage: res -p [path_to_csv]")
                    .build());
            options.addOption(Option.builder("bm")
                    .hasArg()
                    .desc("run a benchmark on the system\n" +
                            "Note that this method should run write after init!\n" +
                            "Usage: bm -t [transaction size] -s [amount of loaded transactions] -p [path to csv]")
                    .build());

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                if (!recorded.get()) {
                    Statistics.deactivate();
                    writeSummery(outPath);
////                writeToScv(outPath);
////                writeBlocksStatistics(outPath);
//                    writeBlocksStatisticsSummery(outPath);
//                    DBUtils.shutdown();
//                    DiskUtils.shutdown();
                }
//
            }));
        }


        void parse(String[] args) throws InterruptedException, IOException {
//            CommandLineParser parser = new DefaultParser();
//            try {
//                CommandLine line = parser.parse(options, args);
                if (args[0].equals("help")) {
                    help();
                    return;
                }
                if (args[0].equals("init")) {
                    init();
                    System.out.println("Init server... [OK]");
                    return;
                }

//                if (args[0].equals("latency")) {
//                    latency();
//                    System.out.println("latency... [OK]");
//                    return;
//                }

                if (args[0].equals("serve")) {
                    serve();
//                    totalRT = System.currentTimeMillis();
                    System.out.println("Serving... [OK]");
                    return;
                }
            if (args[0].equals("writeBlocks")) {
                writeBlocks();
                return;
            }
                if (args[0].equals("stop")) {
                    stop();
//                    totalRT = System.currentTimeMillis() - totalRT;
                    System.out.println("stop server... [OK]");
                    return;
                }
                if (args[0].equals("quit")) {
                    writeSummery(outPath);
//                    writeBlocksStatisticsSummery(outPath);

                    recorded.set(true);
//                    System.out.println("Goodbye :)");
                    System.exit(0);
                    return;
                }

                if (args[0].equals("wait")) {
                    if (args.length == 2) {
                        int sec = Integer.parseInt(args[1]);
                        if (sec > 0) {

                            System.out.println(format("waits for %d seconds ", sec));
                            Thread.sleep(sec * 1000);
                            totalRT = System.currentTimeMillis();
                        }
                    }
                    return;
                }

                if (args[0].equals("async")) {
                    if (!JToy.type.equals("a") && !JToy.type.equals("b")) {
                        logger.debug("Unable to set async behaviour to a correct server");
                        return;
                    }
                    if (args.length == 3) {
                        int sec = Integer.parseInt(args[1]);
                        int duration = Integer.parseInt(args[2]);
                        asyncPeriod(sec * 1000, duration * 1000);
                    }
                    return;
                }

                if (args[0].equals("byz")) {
//                    if (!JToy.type.equals("bs") && !JToy.type.equals("bf")) {
//                        logger.debug("Unable to set byzantine behaviour to non async server");
//                        return;
//                    }
                    setByzSetting(args);
                    return;
                }

//                if (args[0].equals("res")) {
//                    if (args.length == 3) {
//                        if (!args[1].equals("-p")) return;
//                        String path = args[2];
//                        writeToScv(path);
//
//                    }
//                    return;
//                }

                if (args[0].equals("sigTest")) {
                    logger.info("Accepted sigTest");
                    sigTets(outPath);
                    return;

                }

            if (args[0].equals("stStart")) {
               Statistics.activate();
                return;

            }

            if (args[0].equals("stStop")) {
                Statistics.deactivate();
                return;

            }
//                if (args[0].equals("bm")) {
//                    if (args.length != 7) return;
//                    if (!args[1].equals("-t")) return;
//                    if (!args[3].equals("-s")) return;
//                    if (!args[5].equals("-p")) return;
//                    runBenchMark(Integer.parseInt(args[2]), Integer.parseInt(args[4]), args[6]);
//                    return;
//                }

                if (args[0].equals("status")) {
                    if (args.length == 5) {
                        int channel = Integer.parseInt(args[1]);
                        int pid = Integer.parseInt(args[2]);
                        int bid = Integer.parseInt(args[3]);
                        int tid  = Integer.parseInt(args[4]);

                        String stat = txStatus(channel, pid, bid, tid);
                        System.out.println(format("[channel=%d ; pid:%d ; bid=%d ; tid=%d] status=%s", channel, pid,
                                bid, tid, stat));
                        return;
                    }
                }
                logger.error(format("Invalid command %s", Arrays.toString(args)));
//            } catch (Exception e) {
//                logger.error("", e);
//            }

        }
        private void help() {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("Optimistic Total Ordering System ", options);
        }

        private void init() {
            JToy.s.start();
        }

        private void serve() {
            if (JToy.type.equals("m")) return;
            logger.debug("start serving");
            JToy.s.serve();
        }
        private void stop() {
//            JToy.s.stop();
            JToy.s.shutdown();
        }

//        private void latency() throws InterruptedException {
//            init();
//            serve();
//            while (true) {
//                Thread.sleep(10 * 60 * 1000);
//            }
//        }
        private Types.txID addtx(String data, int clientID) {
            return JToy.s.addTransaction(data.getBytes(), clientID);
        }

        private String txStatus(int channel, int pid, int bid, int tid) throws InterruptedException {
            Types.txID txid = Types.txID.newBuilder()
                    .setProposerID(pid)
                    .setBid(bid)
                    .setTxNum(tid)
                    .setChannel(channel)
                    .build();
            int stat = JToy.s.status(txid, false);

            switch (stat) {
                case -1: return "Not exist";
                case 0: return "Approved";
//                case 1: return "Proposed";
//                case 2: return "Approved";
//                case 3: return "Pending";
            }
            return null;
        }
        private void signBlockFromBuilder(Types.Block.Builder b) {
            byte[] tHash = hashBlockData(b.build());
//            for (Types.Transaction t : b.getDataList()) {
//                tHash = DigestMethod.hash(ArrayUtils.addAll(tHash, t.toByteArray()));
//            }
            b.setHeader(b.getHeader().toBuilder()
                    .setProof(blockDigSig.sign(b.getHeader()))).build();
        }
        private Types.Block.Builder createBlock(int txSize) {
            Types.Block.Builder bb = Types.Block.newBuilder();
            for (int i = 0 ; i < Config.getMaxTransactionsInBlock() ; i++) {
                SecureRandom random = new SecureRandom();
                byte[] tx = new byte[txSize];
                random.nextBytes(tx);
                bb.addData(Types.Transaction.newBuilder()
                        .setId(Types.txID.newBuilder()
                                .setTxNum(0)
                                .setProposerID(0)
                                .setChannel(0))
                        .setClientID(0)
                        .setData(ByteString.copyFrom(tx))
                        .build());
            }

            SecureRandom random = new SecureRandom();
            byte[] tx = new byte[32];
            random.nextBytes(tx);

            return bb.setHeader(Types.BlockHeader.newBuilder()
                    .setM(Types.Meta.newBuilder()
                            .setChannel(0)
                            .setCidSeries(0)
                            .setCid(0).build())
                    .setHeight(0)
                    .setBid(Types.BlockID.newBuilder().setBid(0).setPid(0).build())
                    .setPrev(ByteString.copyFrom(tx)));


        }
        private void sigTets(String pathString) throws IOException, InterruptedException {
            logger.info(format("Starting sigTest [%d, %d]", Config.getTxSize(), Config.getMaxTransactionsInBlock()));
            ExecutorService executor = Executors.newFixedThreadPool(Config.getC());
            int bareTxSize = Types.Transaction.newBuilder()
                    .setClientID(0)
                    .setId(Types.txID.newBuilder()
                            .setTxNum(0)
                            .setProposerID(0)
                            .setChannel(0))
//                    .setClientTs(System.currentTimeMillis())
//                    .setServerTs(System.currentTimeMillis())
                    .build().getSerializedSize();
            int tSize = max(0, Config.getTxSize() - bareTxSize);

            CountDownLatch latch1 = new CountDownLatch(Config.getC());
            AtomicInteger avgSig = new AtomicInteger(0);
            AtomicBoolean stop = new AtomicBoolean(false);
            long start = System.currentTimeMillis();
            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
            for (int c = 0 ; c < Config.getC() ; c++) {
                int finalC = c;
                (executor).submit(() -> {
                    Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
                    int sigCount = 0;
                    Types.Block.Builder b = createBlock(tSize);
                    while (!stop.get()) {
                        signBlockFromBuilder(b);
                        sigCount++;
                    }
                    logger.info(format("finishing verForExec [%d] avg is [%d]", finalC, sigCount));
                    avgSig.addAndGet(sigCount);
                    latch1.countDown();
                });
            }
            logger.info(format("Await termination 1"));
            Thread.sleep(60 * 1000);
            stop.set(true);
//            executor.awaitTermination(120, TimeUnit.SECONDS);
            latch1.await();
            int total = (int) ((System.currentTimeMillis() - start) / 1000);
            executor.shutdownNow();
            logger.info(format("res [%d, %d]",  avgSig.get(), total));
            int sigPerSec = avgSig.get() / total;
            int verPerSec = 0; //avgVer.get() / Config.getC();
            Path path = Paths.get(pathString,   String.valueOf(0), "sig_summery.csv");
            File f = new File(path.toString());
            if (!f.exists()) {
                f.getParentFile().mkdirs();
                f.createNewFile();
            }
            logger.info(format("Collecting results"));
            FileWriter writer = new FileWriter(path.toString(), true);
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss");
            List<String> row = Arrays.asList(dateFormat.format(new Date()),
                    String.valueOf(Config.getC()),
                    String.valueOf(bareTxSize + tSize),
                    String.valueOf(Config.getMaxTransactionsInBlock()),
                    String.valueOf(sigPerSec));
            CSVUtils.writeLine(writer, row);
            writer.flush();
            writer.close();
        }
//        private void writeToScv(String pathString) {
//            if (JToy.type.equals("m")) return;
//            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss");
//            Path path = Paths.get(pathString, String.valueOf(JToy.s.getID()), dateFormat.format(new Date()) + ".csv");
//            try {
//                File f = new File(path.toString());
//
//                f.getParentFile().mkdirs();
//                f.createNewFile();
//                FileWriter writer = new FileWriter(path.toString());
//                int nob = JToy.s.getBCSize();
//                long fts = 0;
//                long lts = 0;
//                int tCount = 0;
//                int txSize = -1;
//                fts = JToy.s.nonBlockingDeliver(1).getSt().getDecided();
//                lts = JToy.s.nonBlockingDeliver(nob - 1).getSt().getDecided();
//                tCount = (nob - 1) * Config.getMaxTransactionsInBlock();
//                txSize = JToy.s.nonBlockingDeliver(1).getData(0).getSerializedSize();
//                for (int i = 0 ; i < nob ; i++) {
//                    Types.Block b = JToy.s.nonBlockingDeliver(i);
//                    for (Types.Transaction t : b.getDataList()) {
//                        if (fts == 0) {
//                            fts = b.getSt().getDecided();
//                            lts = fts;
//                        }
//                        fts = min(fts, b.getSt().getDecided());
//                        lts = max(lts, b.getSt().getDecided());
//                        tCount++;
//                        if (txSize == -1) {
//                            txSize = t.getSerializedSize();
//                        }
//                        List<String> row = Arrays.asList(format("[%d:%d]", t.getId().getProposerID(), t.getId().getTxNum())
//                                , String.valueOf(t.getSerializedSize()),
//                                String.valueOf(t.getClientID()), String.valueOf(b.getSt().getDecided()),
//                                String.valueOf(t.getData().size()), String.valueOf(i),
//                                String.valueOf(b.getHeader().getBid().getPid()));
//                        CSVUtils.writeLine(writer, row);
//                    }
//                }
//                writer.flush();
//                writer.close();
////                writeSummery(pathString, tCount, txSize, fts, lts);
//            } catch (IOException e) {
//                logger.error("", e);
//            }
//        }

//        boolean validateBC() {
//            return JToy.s.isValid();
////            for (int i = 1 ; i < JToy.s.getBCSize() ; i++) {
////                if (!validateBlockHash(JToy.s.nonBlockingDeliver(i - 1),
////                        JToy.s.nonBlockingDeliver(i))) {
////                    System.out.println(String.format("Invalid Blockchain!! [%d -> %d]", i-1, i));
////                    return false;
////                }
////            }
////            return true;
//        }

        void writeBlocks() {
            logger.info("Starting writeBlocks");
            String pathString = "/tmp/JToy/res/";
            Path path = Paths.get(pathString, String.valueOf(JToy.s.getID()), "bsummery.csv");
            File f = new File(path.toString());

            try {
                if (!f.exists()) {
                    f.getParentFile().mkdirs();
                    f.createNewFile();
                }
                FileWriter writer = null;
                writer = new FileWriter(path.toString(), true);
                List<List<String>> rows = new ArrayList<>();
                int cw = 0;
                int workers = Config.getC();
                for (int i = Statistics.getH1() ; i < Statistics.getH2() ; i++) {
                    List<Types.Block> rBlocks = new ArrayList<>();
                    for (int  j = 0 ; j < workers ; j++) {
                        rBlocks.add(BCS.nbGetBlock(j, i));

                    }
                    long dlt = Collections.max(rBlocks.stream().map(b ->
                            b.getHeader().getHst().getDefiniteTime()).collect(Collectors.toList()));
                    for (int j = 0 ; j < workers ; j++) {
                        Types.Block b = rBlocks.get(j);
                        long pt = b.getBst().getProposeTime();
                        long tt = b.getHeader().getHst().getTentativeTime();
                        long dt = b.getHeader().getHst().getDefiniteTime();
//                        if (dlt == -1) {
//                            logger.info(format("Un registered block [h=%d ; w=%d]", i, j));
//                            continue;
//                        }
                        rows.add(Arrays.asList(
                                    String.valueOf(j)
                                    , String.valueOf(b.getId().getPid())
                                    , String.valueOf(b.getId().getBid())
                                    , String.valueOf(b.getHeader().getHeight())
                                    , String.valueOf(b.getDataCount())
                                    , String.valueOf(tt - pt)
                                    , String.valueOf(dt - pt)
                                    , String.valueOf(dlt - pt)
                                    , String.valueOf(dt - tt)
                                    , String.valueOf(dlt - dt)
                                )

                        );
                    }
                }
                CSVUtils.writeLines(writer, rows);
                writer.flush();
                writer.close();
                logger.info("ended writeBlocks");
            } catch (Exception e) {
                logger.error(e);
            }
        }
        void writeSummery(String pathString) {
            logger.info("Starting writeSummery");
            Path path = Paths.get(pathString, String.valueOf(JToy.s.getID()), "summery.csv");
            File f = new File(path.toString());

            try {
                if (!f.exists()) {
                    f.getParentFile().mkdirs();
                    f.createNewFile();
                }
                FileWriter writer = null;
                writer = new FileWriter(path.toString(), true);

                int nob = Statistics.getNob();
                int noeb = Statistics.getNeb();
                int txCount = Statistics.getTxCount();
                long txSize = Config.getTxSize();
                int txInBlock = Config.getMaxTransactionsInBlock();
//                int h1 = Statistics.getH1();
//                int h2 = Statistics.getH2();
                int avgTxInBlock = 0;
                double acBP2T = Statistics.getAcBP2T();
                double acBP2D = Statistics.getAcBP2D();
                double acBP2DL = Statistics.getAcBP2DL();

                double acHP2T = Statistics.getAcHP2T();
                double acHP2D = Statistics.getAcHP2D();
                double acHT2D = Statistics.getAcHT2D();
                double acHP2DL = Statistics.getAcHP2DL();
                double acHD2DL = Statistics.getAcHD2DL();

                long time = (Statistics.getStop() - Statistics.getStart()) / 1000;
                if (nob > 0) {
                    avgTxInBlock = txCount / nob;

                }

                if (nob > 0) {
                    txInBlock = txCount / nob;
                }
                double BP2T = 0;
                double BP2D = 0;
                double BP2DL = 0;

                double HP2T = 0;
                double HP2D = 0;
                double HT2D = 0;
                double HP2DL = 0;
                double HD2DL = 0;

                if (nob > 0) {
                    BP2T = acBP2T / nob;
                    BP2D = acBP2D / nob;
                    BP2DL = acBP2DL / nob;

                    HP2T = acHP2T / nob;
                    HP2D = acHP2D / nob;
                    HT2D = acHT2D / nob;
                    HP2DL = acHP2DL / nob;
                    HD2DL = acHD2DL / nob;
                }

                int tps = 0;
                int bps = 0;
                if (time > 0) {
                    tps = ((int) (txCount / time));
                    bps = (int) (nob / time);
                }

                int pos = Statistics.getPos();
                int neg = Statistics.getNeg();
                int opt = Statistics.getOpt();
                int all = Statistics.getAll();

                double opRate = 0;
                double posRate = 0;
                double negRate = 0;
                double avgNegDecTime = 0;
                long avgTmo = 0;
                long avgActTmo = 0;

                if (pos > 0) {
                    opRate = ((double) opt) / ((double) pos);
                }
                if (all > 0) {
                    posRate = ((double) pos) / ((double) all);
                    negRate = ((double) neg) / ((double) all);
                    avgTmo = Statistics.getTmo() / all;
                    avgActTmo = Statistics.getActTmo() / all;
                }
                if (neg > 0) {

                    avgNegDecTime = ((double) Statistics.getNegTime() / (double) neg);
                }
                int syncEvents = Statistics.getSyncs();
                boolean valid = true; //BCS.isValid();

                List<String> row = Arrays.asList(
                        String.valueOf(valid)
                        , String.valueOf(JToy.s.getID())
                        , JToy.type
                        , String.valueOf(Config.getC())
                        , String.valueOf(avgTmo)
                        , String.valueOf(avgActTmo)
                        , String.valueOf(Statistics.getMaxTmo())
                        , String.valueOf(txSize)
                        , String.valueOf(txInBlock)
                        , String.valueOf(txCount)
                        , String.valueOf(time)
                        , String.valueOf(tps)
                        , String.valueOf(nob)
                        , String.valueOf(noeb)
                        , String.valueOf(bps)
                        , String.valueOf(avgTxInBlock)
                        , String.valueOf(opt)
                        , String.valueOf(opRate)
                        , String.valueOf(pos)
                        , String.valueOf(posRate)
                        , String.valueOf(neg)
                        , String.valueOf(negRate)
                        , String.valueOf(avgNegDecTime)
                        , String.valueOf(syncEvents)
                        , String.valueOf(BP2T)
                        , String.valueOf(BP2D)
                        , String.valueOf(BP2DL)
                        , String.valueOf(HP2T)
                        , String.valueOf(HP2D)
                        , String.valueOf(HP2DL)
                        , String.valueOf(HT2D)
                        , String.valueOf(HD2DL)
                        , String.valueOf(BFD.getSuspected())
                );
                CSVUtils.writeLine(writer, row);
                writer.flush();
                writer.close();
                logger.info("ended writeSummery");
            } catch (IOException e) {
                logger.error(e);
            }

        }

//    void writeBlocksStatisticsSummery(String pathString)  {
//        Path path = Paths.get(pathString,   String.valueOf(JToy.s.getID()), "blocksStatSummery.csv");
//        try {
//            File f = new File(path.toString());
//            if (!f.exists()) {
//                f.getParentFile().mkdirs();
//                f.createNewFile();
//            }
//            FileWriter writer = null;
//            writer = new FileWriter(path.toString(), true);
//            int nob = JToy.s.getBCSize();
////            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss");
//            Statistics st = JToy.s.getStatistics();
////            long total = 0;
//            long signaturePeriod = 0;
////            long create2propose = 0;
//            long verificationPeriod = 0;
//            long propose2tentative = 0;
//            long tentative2permanent = 0;
//            long channelPermanent2decide = 0;
//            long propose2permanentchannel= 0;
//            long propose2decide = 0;
//            for (int i = 1 ; i < nob ; i++) {
//                Types.Block b = JToy.s.nonBlockingDeliver(i);
//                if (b == null) {
//                    System.out.println(format("[i=%d] and NULL!!!!!", i));
//                    continue;
//                }
//                Types.blockStatistics bst = b.getSt();
//                signaturePeriod += bst.getSign();
////                create2propose += bst.getProposed() - bst.getCreated();
//                verificationPeriod += bst.getVerified();
//                propose2tentative += bst.getChannelDecided() - bst.getProposed();
//                tentative2permanent += bst.getPd() - bst.getChannelDecided();
//                channelPermanent2decide += bst.getDecided() - bst.getPd();
//                propose2permanentchannel += bst.getPd() - bst.getProposed();
//                propose2decide += bst.getDecided() - bst.getProposed();
////                total += b.getSt().getDecided() - b.getSt().getProposed();
//            }
////            long avgTotal = total / nob;
//            signaturePeriod = signaturePeriod / nob;
////            create2propose = create2propose / nob;
//            verificationPeriod = verificationPeriod / nob;
//            propose2tentative = propose2tentative / nob;
//            tentative2permanent = tentative2permanent / nob;
//            channelPermanent2decide = channelPermanent2decide / nob;
//            propose2permanentchannel = propose2permanentchannel / nob;
//            propose2decide = propose2decide / nob;
//            List<String> row = Arrays.asList(String.valueOf(JToy.s.getID()),
//                    JToy.type, String.valueOf(Config.getC()), String.valueOf(st.txSize),
//                    String.valueOf(Config.getMaxTransactionsInBlock()),
//                    String.valueOf(signaturePeriod),
//                    String.valueOf(verificationPeriod),
////                    String.valueOf(create2propose),
//                    String.valueOf(propose2tentative),
//                    String.valueOf(tentative2permanent),
//                    String.valueOf(channelPermanent2decide),
//                    String.valueOf(propose2permanentchannel),
//                    String.valueOf(propose2decide));
////                    String.valueOf(avgTotal));
//            CSVUtils.writeLine(writer, row);
//            writer.flush();
//            writer.close();
//        } catch (IOException e) {
//            logger.error(e);
//        }
//    }

//        void writeBlocksStatistics(String pathString)  {
//            Path path = Paths.get(pathString,   String.valueOf(JToy.s.getID()), "blocksStat.csv");
//            try {
//                File f = new File(path.toString());
//                if (!f.exists()) {
//                    f.getParentFile().mkdirs();
//                    f.createNewFile();
//                }
//                FileWriter writer = null;
//                writer = new FileWriter(path.toString(), true);
//                int nob = JToy.s.getBCSize();
////                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss");
//                Statistics st = JToy.s.getStatistics();
//                for (int i = 1 ; i < nob ; i++) {
//                    Types.Block b = JToy.s.nonBlockingDeliver(i);
////                    logger.info(format("[height=%d] [tentative=%d] [permanent=%d] [diff=%d]",
////                            b.getHeader().getHeight(), b.getSt().getChannelDecided(),
////                            b.getSt().getPd(), b.getSt().getPd() - b.getSt().getChannelDecided()));
//                    Types.blockStatistics bst = b.getSt();
////                    long total = bst.getDecided() - bst.getChannelDecided();
//                    long signaturePeriod = bst.getSign();
//                    long verificationPeriod = bst.getVerified();
////                    long create2propose = bst.getProposed() - bst.getCreated();
//                    long propose2tentative = bst.getChannelDecided() - bst.getProposed();
//                    long tentative2permanent = bst.getPd() - bst.getChannelDecided();
//                    long channelPermanent2decide = bst.getDecided() - bst.getPd();
//                    long propose2permanentchannel= bst.getPd() - bst.getProposed();
//                    long propose2decide = bst.getDecided() - bst.getProposed();
//                    List<String> row = Arrays.asList(String.valueOf(JToy.s.getID()),
//                            JToy.type, String.valueOf(Config.getC()), String.valueOf(st.txSize),
//                            String.valueOf(Config.getMaxTransactionsInBlock()),
//                            String.valueOf(b.getDataCount()),
//                            String.valueOf(b.getHeader().getHeight()),
//                            String.valueOf(signaturePeriod),
//                            String.valueOf(verificationPeriod),
////                            String.valueOf(create2propose),
//                            String.valueOf(propose2tentative),
//                            String.valueOf(tentative2permanent),
//                            String.valueOf(channelPermanent2decide),
//                            String.valueOf(propose2permanentchannel),
//                            String.valueOf(propose2decide));
////                            String.valueOf(total));
//                    CSVUtils.writeLine(writer, row);
//                }
//                writer.flush();
//                writer.close();
//            } catch (IOException e) {
//                logger.error(e);
//            }
//        }

        private void setByzSetting(String[] args) {
            System.out.println("Setting byz");
            boolean fullByz = Integer.parseInt(args[1]) == 1;
            List<List<Integer>> groups = new ArrayList<>();
            int i = 2;
            while (i < args.length) {
                i++;
                List<Integer> group = new ArrayList<>();
                while (i < args.length && !args[i].equals("-g")) {
                    group.add(Integer.parseInt(args[i]));
                    i++;
                }
                groups.add(group);
            }
            JToy.s.setByzSetting(fullByz, groups);
        }

        private void asyncPeriod(int sec, int duration) throws InterruptedException {
            System.out.println(format("setting async params [%d] sec delay for [%d] sec", sec/1000, duration/1000));
            JToy.s.setAsyncParam(sec);
            if (duration > 0) {
                Thread.sleep(duration);
            }
            System.out.println(format("return to normal"));
            JToy.s.setAsyncParam(0);
        }

}
