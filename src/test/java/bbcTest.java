import config.Config;
//import consensus.bbc.bbcClient;
import consensus.bbc.bbcService;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class bbcTest {
    private int timeToWaitBetweenTest = 15 * 1000;
    static Config conf = new Config();
    private final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(bbcTest.class);
    private static Path SingleServerconfigHome = Paths.get("config", "bbcConfig", "bbcSingleServer");
    private static Path FourServerconfigHome = Paths.get("config", "bbcConfig", "bbcFourServers");
    private static void deleteViewIfExist(String configHome){
        File file2 = new File(Paths.get(configHome, "currentView").toString());
        if (file2.exists()) {
            logger.info("Saved view found in " + file2.getAbsolutePath() + " deleting...");
            if (file2.delete()) {
                logger.info(file2.getName() + " has been deleted");
            } else {
                logger.warn(file2.getName() + " could not be deleted");
            }
        }
    }
    @Test
    void initServerTest() throws InterruptedException {
        Thread.sleep(timeToWaitBetweenTest);
        logger.info("Testing initServerTest...");
//        serversUp = 0;
        CountDownLatch latch = new CountDownLatch(1);
        deleteViewIfExist(SingleServerconfigHome.toString());
        bbcService s = new bbcService(0, 1, SingleServerconfigHome.toString());
        Thread t = new Thread(()-> {
            s.start();
            latch.countDown();
        });
        t.start();
        latch.await();
        s.shutdown();
        t.join();

    }
    @Test
    void testSingleDecision() throws InterruptedException {
        Thread.sleep(timeToWaitBetweenTest);
        logger.info("Testing testSingleDecision...");
        CountDownLatch latch = new CountDownLatch(1);
        deleteViewIfExist(SingleServerconfigHome.toString());
        bbcService s = new bbcService(0, 1, SingleServerconfigHome.toString());
        Thread t1 = new Thread(()-> {
            s.start();
            latch.countDown();
        });
        t1.start();
        latch.await();
        s.propose(1, 0);
        s.propose(0, 1);
        assertEquals(1, s.decide(0).getDecosion());
        assertEquals(0, s.decide(1).getDecosion());
        s.shutdown();
        t1.join();

    }
    @Test
    void testFourServersAllCorrect1() throws InterruptedException {
        Thread.sleep(timeToWaitBetweenTest);
        logger.info("Testing testFourServersAllCorrect1...");
        deleteViewIfExist(FourServerconfigHome.toString());
        int consID = 0;
        CountDownLatch latch = new CountDownLatch(4);
        Thread[] serversThread = new Thread[4];
        bbcService[] servers = new bbcService[4];
        for (int i = 0 ; i < 4 ; i++) {
            servers[i] = new bbcService(i, 3, FourServerconfigHome.toString());
            int finalI = i;
            serversThread[i] = new Thread(() -> { servers[finalI].start();
                latch.countDown();
            });
            serversThread[i].start();
        }
        latch.await();
        for (int i = 0 ; i < 4 ; i++) {
            servers[i].propose(consID % 2, consID);
        }

        for (int i = 0 ; i < 4 ; i++) {
            assertEquals(consID % 2, servers[i].decide(consID).getDecosion());
        }
        consID++;
        for (int i = 0 ; i < 4 ; i++) {
            servers[i].propose(consID % 2, consID);
        }

        for (int i = 0 ; i < 4 ; i++) {
            assertEquals(consID % 2, servers[i].decide(consID).getDecosion());
        }
        for (int i = 0 ; i < 4 ; i++) {
            servers[i].shutdown();
        }
        for (int i = 0 ; i < 4 ; i++) {
            serversThread[i].join();
        }
        logger.info("End of testFourServersAllCorrect1");

    }
    @Test
    void testFourServersBasicFault1() throws InterruptedException {
        Thread.sleep(timeToWaitBetweenTest);
        CountDownLatch count = new CountDownLatch(4);
        logger.info("Testing testFourServersBasicFault1...");
        deleteViewIfExist(FourServerconfigHome.toString());
        int consID = 0;
        Thread[] serversThread = new Thread[4];
        bbcService[] servers = new bbcService[4];
        for (int i = 0 ; i < 4 ; i++) {
            servers[i] = new bbcService(i, 3, FourServerconfigHome.toString());
            int finalI = i;
            serversThread[i] = new Thread(() -> {servers[finalI].start();
                count.countDown();
            });
            serversThread[i].start();
        }
        count.await();

        for (int i = 0 ; i < 4 ; i++) {
            for (int k = 0 ; k < 4 ; k++) {
                if (i == k) continue;
                servers[k].propose(consID % 2, consID);
            }
            for (int k = 0 ; k < 4 ;k++) {
                assertEquals(consID % 2, servers[k].decide(consID).getDecosion());
            }
            consID++;
        }
        for (int i = 0 ; i < 4 ; i++) {
            servers[i].shutdown();
        }
        for (int i = 0 ; i < 4 ; i++) {
            serversThread[i].join();
        }

    }

    @Test
    void stressTestFourServersAllCorrect() throws InterruptedException {
        Thread.sleep(timeToWaitBetweenTest);
        logger.info("Testing testFourServersAllCorrect1...");
        deleteViewIfExist(FourServerconfigHome.toString());
        int consID = 0;
        CountDownLatch latch = new CountDownLatch(4);
        Thread[] serversThread = new Thread[4];
        bbcService[] servers = new bbcService[4];
        for (int i = 0 ; i < 4 ; i++) {
            servers[i] = new bbcService(i, 3, FourServerconfigHome.toString());
            int finalI = i;
            serversThread[i] = new Thread(() -> {servers[finalI].start();
                latch.countDown();
            });
            serversThread[i].start();
        }
        latch.await();
        for (int i = 0 ; i < 100 ; i++) {
            for (int k = 0 ; k < 4 ; k++) {
                servers[k].propose(consID % 2, consID);
            }
            for (int k = 0 ; k < 4 ; k++) {
                assertEquals(consID % 2, servers[k].decide(consID).getDecosion());
            }
            consID++;

        }
        for (int i = 0 ; i < 4 ; i++) {
            servers[i].shutdown();
        }

        for (int i = 0 ; i < 4 ; i++) {
            serversThread[i].join();
        }
    }

    @Test
    void testFourServersFailure2() throws InterruptedException {
        Thread.sleep(timeToWaitBetweenTest);
        logger.info("Testing testFourServersAllCorrect1...");
        deleteViewIfExist(FourServerconfigHome.toString());
        CountDownLatch latch = new CountDownLatch(4);
        Thread[] serversThread = new Thread[4];
        bbcService[] servers = new bbcService[4];
        for (int i = 0 ; i < 4 ; i++) {
            servers[i] = new bbcService(i, 3, FourServerconfigHome.toString());
            int finalI = i;
            serversThread[i] = new Thread(() -> { servers[finalI].start();
                latch.countDown();
            });
            serversThread[i].start();
        }
        latch.await();
        for (int i = 0 ; i < 1000 ; i++) {
            servers[0].propose(0, i);
            servers[1].propose(1, i);
            servers[2].propose(1, i);
        }

        for (int i = 0 ; i < 1000 ; i++) {
            for (int k = 0 ; k < 4 ; k++) {
                assertEquals(1, servers[k].decide(i).getDecosion());
            }

        }
        for (int i = 0 ; i < 4 ; i++) {
            servers[i].shutdown();
        }
        for (int i = 0 ; i < 4 ; i++) {
            serversThread[i].join();
        }
        logger.info("End of testFourServersAllCorrect1");

    }
}
