package se.vierecksauge;

import java.awt.*;

public abstract class GameCore {

        protected static final int FONT_SIZE = 24;

        private boolean isRunning;
        protected ScreenManager screen;

        public void stop() {
            isRunning = false;
        }

        public void run() {
            try {
                init();
                loop();
            }
            finally {
                screen.restoreScreen();
                System.exit(0);
            }
        }

        public void init() {
            screen = new ScreenManager();

            Window window = screen.getWindow();
            window.setFont(new Font("Dialog", Font.PLAIN, FONT_SIZE));

            isRunning = true;
        }

        public void loop() {
            double currentTime = System.nanoTime();

            while (isRunning) {
                long newTime = System.nanoTime();

                double deltaTime = (newTime - currentTime) / 1_000_000.0;

                currentTime = newTime;

                update(deltaTime);

                Graphics2D g = screen.getGraphics();
                if (g != null) {
                    draw(g);
                    g.dispose();
                }

                screen.update();
            }
        }

        public void update(double delta) {

        }

        public abstract void draw(Graphics2D g);

}
