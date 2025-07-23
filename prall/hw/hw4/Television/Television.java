package Television;

import java.util.Random;

public class Television {
    private final String model;
    private final double diagonal;
    private boolean powerOn;
    private int channel;
    private int volume;
    private final Random random = new Random();

    public Television(String model, double diagonal) {
        this.model = model;
        this.diagonal = diagonal;
        this.powerOn = false;
        this.channel = 1;
        this.volume = 50;
    }

    public String getModel() {
        return model;
    }

    public double getDiagonal() {
        return diagonal;
    }

    public boolean isPowerOn() {
        return powerOn;
    }

    public void power() {
        powerOn = !powerOn;
        if (powerOn) {
            printLine();
            System.out.println("Телевизор включен");
        } else {
            printLine();
            System.out.println("Телевизор выключен");
        }
    }

    public void powerOnRandomChannel() {
    if (!powerOn) {
        printLine();
        System.out.println("Ошибка! TV выключен, сначала его необходимо включить.");
        return;
    }
    this.channel = random.nextInt(20) + 1;
    printLine();
    System.out.println("Включен " + channel + " канал");
    }

    public void setChannel(int ChannelNumber) {
        if (!powerOn) {
            printLine();
            System.out.println("Ошибка! TV выключен, сначала его необходимо включить.");
            return;
        }
        if (ChannelNumber < 1 || ChannelNumber > 20) {
            printLine();
            System.out.println("Ошибка: Номер канала должен быть от 1 до 20");
            return;
        }
        this.channel = ChannelNumber;
        printLine();
        System.out.println("TV переключен на " + ChannelNumber + " канал.");
    }

    public void nextChannel() {
        if (!powerOn) {
            printLine();
            System.out.println("Ошибка! TV выключен, сначала его необходимо включить.");
            return;
        }
        if (this.channel >= 20) {
            this.channel = 1;
        } else {
            this.channel++;
        }
        printLine();
        System.out.println("TV переключен на " + this.channel + " канал.");
    }

    public void prevChannel() {
        if (!powerOn) {
            printLine();
            System.out.println("Ошибка! TV выключен, сначала его необходимо включить.");
            return;
        }

        if (this.channel <= 1) {
            this.channel = 20;
        } else {
            this.channel--;
        }
        printLine();
        System.out.println("TV переключен на " + this.channel + " канал.");
    }

    public void setVolume(int VolumeNumber) {
        if (!powerOn) {
            printLine();
            System.out.println("Ошибка! TV выключен, сначала его необходимо включить.");
            return;
        }
        if (VolumeNumber < 1 || VolumeNumber > 100) {
            printLine();
            System.out.println("Ошибка: Уровень громкости должен быть от 1 до 100");
            return;
        }
        this.volume = VolumeNumber;
        printLine();
        System.out.println("Уровень громкости установлен на " + VolumeNumber + " ед.");
    }

    public void upVolume () {
        if (!powerOn) {
            printLine();
            System.out.println("Ошибка! TV выключен, сначала его необходимо включить.");
            return;
        }
        if (this.volume >= 100) {
            printLine();
            System.out.println("Максимальный уровень громкости (100) уже достигнут!");
        } else {
            volume = Math.min(100, volume + 15);
            printLine();
            System.out.println("Уровень громкости увеличен, текущий: " + this.volume);
        }
    }

    public void downVolume () {
        if (!powerOn) {
            printLine();
            System.out.println("Ошибка! TV выключен, сначала его необходимо включить.");
            return;
        }
        if (this.volume <= 1) {
            printLine();
            System.out.println("Минимальный уровень громкости (1) уже достигнут!");
        } else {
            volume = Math.max(1, volume - 15);
            printLine();
            System.out.println("Уровень громкости уменьшен, текущий: " + this.volume);
        }

    }

    public void tvStatus () {
        if (!powerOn) {
            String tvStatus;
            tvStatus = "Выключен";
            printLine();
            System.out.println("TV: " + tvStatus);
        } else {
            String tvStatus;
            tvStatus = "Включен";
            printLine();
            System.out.println("\033[1mTV: \033[0m" + tvStatus);
            System.out.println("\033[1mТекущий канал: \033[0m" + channel);
            System.out.println("\033[1mУровень громкости: \033[0m" + volume);
        }
    }

    public void printLine () {
        System.out.println("= = = = = = = = = = = = = = =");
    }
}
