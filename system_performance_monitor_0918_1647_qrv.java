// 代码生成时间: 2025-09-18 16:47:53
import io.quarkus.runtime.Quarkus;
    import io.quarkus.runtime.annotations.QuarkusMain;
    import org.openjdk.jol.info.ClassLayout;
    import java.lang.management.ManagementFactory;
    import java.lang.management.RuntimeMXBean;
    import java.lang.management.MemoryMXBean;
    import java.lang.management.MemoryUsage;
    import java.lang.management.ThreadInfo;
    import java.lang.management.ThreadMXBean;
    import java.time.LocalDateTime;
    import java.time.format.DateTimeFormatter;

    // 主类，包含入口方法
    @QuarkusMain
    public class SystemPerformanceMonitor {

        // 主方法，用于启动监控工具
        public static void main(String... args) {
            try {
                RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
                MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
                ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();

                // 获取并打印系统运行时间
                long startTime = runtimeMXBean.getStartTime();
                LocalDateTime startDateTime = LocalDateTime.ofEpochSecond(startTime / 1000, 0, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                System.out.println("System Uptime: " + startDateTime);

                // 获取并打印内存信息
                MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
                MemoryUsage nonHeapMemoryUsage = memoryMXBean.getNonHeapMemoryUsage();
                System.out.println("Heap Memory Usage: " + heapMemoryUsage);
                System.out.println("Non-Heap Memory Usage: " + nonHeapMemoryUsage);

                // 获取并打印线程信息
                long[] threadIds = threadMXBean.getAllThreadIds();
                for (long threadId : threadIds) {
                    ThreadInfo threadInfo = threadMXBean.getThreadInfo(threadId);
                    System.out.println("Thread ID: " + threadId);
                    System.out.println("Thread Name: " + threadInfo.getThreadName());
                    System.out.println("Thread State: " + threadInfo.getThreadState());
                    System.out.println();
                }

                // 打印类的内存布局信息
                printClassMemoryLayout();

            } catch (Exception e) {
                System.out.println("Error occurred: " + e.getMessage());
                e.printStackTrace();
            }
        }

        // 方法：打印类的内存布局信息
        private static void printClassMemoryLayout() {
            System.out.println("Class Memory Layout: ");
            System.out.println(ClassLayout.parseClass(SystemPerformanceMonitor.class).toPrintable());
        }
    }