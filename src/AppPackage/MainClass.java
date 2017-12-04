package AppPackage;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class MainClass 
{    
    public Player player;
    public long pauseLocation;
    public long songTotalLength;
    public String fileLocation;
    boolean play = false;
    FileInputStream FIS;
    BufferedInputStream BIS;

    public void setPlay(boolean Play) {
    this.play = Play;
    }
    public boolean getPlay() {
    return this.play;
    }
    public void Stop()
    {
    
        if(player != null) {
         setPlay(false);
         player.close();
         pauseLocation = 0;
         songTotalLength = 0;
         fileLocation = "";
         MP3PlayerGUI.Display.setText("Stop");
         
        }
        
    }
    public void Pause()
    {
    setPlay(false);
        if(player != null) {
            try {
                
                pauseLocation = FIS.available();
                player.close();
                MP3PlayerGUI.Display.setText("Pause");
                
            }  catch (IOException ex) {
               
            }
        }
        
    }
    public void Play(String path)
    {
        this.setPlay(true);
        try {
            
            FIS = new FileInputStream(path);
            BIS = new BufferedInputStream(FIS);
            
            player = new Player(BIS);
            songTotalLength = FIS.available();
            fileLocation = path +"";
            
            try {
                FIS.available();
            } catch (IOException ex) {
                
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Arquivo não encontrado - "+ex.getMessage());
        } catch (JavaLayerException ex) {
            System.out.println("Erro - "+ex.getMessage());
        } catch (IOException ex) {
            System.out.println("Erro - "+ex.getMessage());
        }
            
        new Thread() 
        {
        @Override
        public void run() {
            try {
                
                player.play();
                
                
                if(player.isComplete() && MP3PlayerGUI.count ==1) {
                    Play(fileLocation);
            }
                if(player.isComplete()) {
                MP3PlayerGUI.Display.setText("");
                }
                
            } catch (JavaLayerException ex) {
                System.out.println("Erro - "+ex.getMessage());
            }
        }
        
        }.start();
    }
    public void Resume()
    {
        if(!this.getPlay()) {
        try {
            
            FIS = new FileInputStream(fileLocation);
            BIS = new BufferedInputStream(FIS);
            play = true;
            player = new Player(BIS);
            MP3PlayerGUI.Display.setText(MP3PlayerGUI.name);
            FIS.skip(songTotalLength - pauseLocation);
            
        } catch (FileNotFoundException ex) {
            System.out.println("Arquivo não encontrado - "+ex.getMessage());
        } catch (JavaLayerException ex) {
            System.out.println("Erro - "+ex.getMessage());
        } catch (IOException ex) {
            System.out.println("Erro - "+ex.getMessage());
        }
            
        new Thread() 
        {
        @Override
        public void run() {
            try {
                
                player.play();
                
                
            } catch (JavaLayerException ex) {
                System.out.println("Erro - "+ex.getMessage());
            }
        }
        
        }.start();
    } else {
        System.out.println("Já está em execução");
        this.setPlay(true);
        
        }
    }
} 
