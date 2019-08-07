
package com.geekfoxer.gateway.server.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author pizhihui
 * @date 2019-08-06
 */
public abstract class AbstractScheduleCache {
  private static final Logger LOGGER = LoggerFactory.getLogger(AbstractScheduleCache.class);
  private static final int cpu = Runtime.getRuntime().availableProcessors();
  private static final ScheduledExecutorService SCHEDULE_EXCUTOR =
      Executors.newScheduledThreadPool(cpu, new NamedThreadFactory());
  private long INTERVAL = 60;

  @PostConstruct
  public void doSchedule() {
    SCHEDULE_EXCUTOR.scheduleAtFixedRate(new Runnable() {

      @Override
      public void run() {
        try {
          doCache();
        } catch (Throwable e) {
          LOGGER.error(e.getMessage(), e);
        }

      }
    }, 0, INTERVAL, TimeUnit.SECONDS);
  }

  protected abstract void doCache();

  private static class NamedThreadFactory implements ThreadFactory {
    private static final AtomicInteger POOL_SEQ = new AtomicInteger(1);

    private final AtomicInteger mThreadNum = new AtomicInteger(1);

    private final String mPrefix;

    private final boolean mDaemon;

    private final ThreadGroup mGroup;

    public NamedThreadFactory() {
      this("TeslaScheduleCache-" + POOL_SEQ.getAndIncrement(), true);
    }

    public NamedThreadFactory(String prefix, boolean daemon) {
      mPrefix = prefix + "-thread-";
      mDaemon = daemon;
      SecurityManager s = System.getSecurityManager();
      mGroup = (s == null) ? Thread.currentThread().getThreadGroup() : s.getThreadGroup();
    }

    @Override
    public Thread newThread(Runnable runnable) {
      String name = mPrefix + mThreadNum.getAndIncrement();
      Thread ret = new Thread(mGroup, runnable, name, 0);
      ret.setDaemon(mDaemon);
      return ret;
    }

  }



}
