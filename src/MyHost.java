import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class MyHost extends Host {

    private final PriorityBlockingQueue<Task> taskQueue = new PriorityBlockingQueue<>(10,
            (task1, task2) -> task1.getPriority() == task2.getPriority() ?
                    Integer.compare(task1.getId(), task2.getId()) :
                    Integer.compare(task2.getPriority(), task1.getPriority())
    );

    private final AtomicBoolean isPreempted = new AtomicBoolean(false);
    private final AtomicReference<Task> currentTask = new AtomicReference<>(null);
    private final AtomicBoolean isRunning = new AtomicBoolean(true);
    private final Object lock = new Object();
    private final AtomicLong remainingWorkTime = new AtomicLong(0);

    @Override
    public void run() {
        while (isRunning.get()) {
            remainingWorkTime.set(0);
            waitForTask();

            Task task = taskQueue.poll();
            currentTask.set(task);
            if (task != null) {
                processTask();
            }
        }
    }

    private void processTask() {
        long startTime = (long) (Timer.getTimeDouble() * 1000);
        long taskTime = currentTask.get().getLeft();
        remainingWorkTime.set(taskTime);
        long currTime = executeTask(startTime, taskTime);


        if (currTime < taskTime) {
            currentTask.get().setLeft(taskTime - currTime);
            taskQueue.add(currentTask.get());
        } else {
            currentTask.get().finish();
            currentTask.set(null);
        }
    }

    private void waitForTask() {
        while (taskQueue.isEmpty() && isRunning.get()) {
            synchronized (lock) {
                try {
                    lock.wait(2000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    private long executeTask(long startTime, long taskTime) {
        long currTime = 0;

        while (currTime < taskTime && isRunning.get()) {
            if (isPreempted.get()) {
                isPreempted.set(false);
                break;
            }

            currTime = (long) (Timer.getTimeDouble() * 1000 - startTime);
            remainingWorkTime.set(Math.max(0, taskTime - currTime));
        }

        return currTime;
    }

    @Override
    public void addTask(Task task) {
        try {
            taskQueue.add(task);

            synchronized (lock) {
                lock.notifyAll();
            }

        } catch (Exception e) {
            System.out.println("Error adding task to the queue.");
        }

        preemptRunningTask(task);
    }

    private void preemptRunningTask(Task task) {
        if (currentTask.get() != null && currentTask.get().isPreemptible()
                && task.getPriority() > currentTask.get().getPriority()) {
            isPreempted.set(true);
        }
    }

    @Override
    public int getQueueSize() {
        if (currentTask.get() != null) {
            return taskQueue.size() + 1;
        } else {
            return taskQueue.size();
        }
    }

    @Override
    public long getWorkLeft() {
        return taskQueue.stream().mapToLong(Task::getLeft).sum() + remainingWorkTime.get();
    }

    @Override
    public void shutdown() {
        isRunning.set(false);
    }
}
