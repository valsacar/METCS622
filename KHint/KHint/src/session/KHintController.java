package session;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Region;
import targets.DragTarget;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import fragments.Fragment;
import hints.Hint;

/**
 * @author Joseph Monk
 *
 * Controls the main application, loads the problem into DragTargets which are displayed and support drag-and-drop to re-order.
 */

public class KHintController implements Initializable {


	@FXML
	private ListView<DragTarget> orderList;
	
	@FXML
    private TextArea descBox = new TextArea();
	
	@FXML
    private Label score;


	private ObservableList<DragTarget> targets; // Targets that will be displayed
	private KHintProblem myProblem;

	/*
	 * Precondition1: A problem containing targets with randomized current fragment
	 */
	public KHintController(KHintProblem problem) {
		this.targets = FXCollections.observableArrayList(problem.getTargets());
		this.myProblem = problem;
	}
	
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		orderList.setItems(targets); // Put the targets in the view
		orderList.setCellFactory(param -> new TargetCell());
		
		this.descBox.setText(myProblem.getDescription());
		this.descBox.setWrapText(true);
		score.setText("100%");
	}
	
	/*
	 * Inner class to setup the target cells and enable them to be re-ordered with drag and drop
	 */
	private class TargetCell extends ListCell<DragTarget> {

        public TargetCell() {
            ListCell<DragTarget> thisCell = this;
            this.setWrapText(true);

            setContentDisplay(ContentDisplay.TEXT_ONLY);
            setAlignment(Pos.CENTER_LEFT);

            setOnDragDetected(event -> {
                if (getItem() == null) { 
                	event.consume();
                    return;
                }

                ObservableList<DragTarget> items = getListView().getItems();

                Dragboard dragboard = startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putString(String.valueOf(items.indexOf(getItem())));
                //dragboard.setDragView();  Maybe I'll find some icon to use here, but it appears broken on Linux so I can't test

                dragboard.setContent(content);

                event.consume();
                
            });

            setOnDragOver(event -> {
                if (event.getGestureSource() != thisCell &&
                       event.getDragboard().hasString()) {
                    event.acceptTransferModes(TransferMode.MOVE);
                }

                event.consume();
            });

            setOnDragEntered(event -> {
                if (event.getGestureSource() != thisCell &&
                        event.getDragboard().hasString()) {
                    setOpacity(0.3);
                }
            });

            setOnDragExited(event -> {
                if (event.getGestureSource() != thisCell &&
                        event.getDragboard().hasString()) {
                    setOpacity(1);
                }
            });

            setOnDragDropped(event -> {
                if (getItem() == null) { 
                	event.consume();
                    return;
                }                
            
                Dragboard db = event.getDragboard();	                
                boolean success = false;
                
                if (db.hasString()) {
                    int draggedIdx = Integer.valueOf(db.getString());


                    DragTarget tempTarg = targets.get(draggedIdx);
                    Fragment tempFrag = tempTarg.getCurrentFrag();
                    tempTarg.setCurrentFrag(getItem().getCurrentFrag());
                    getItem().setCurrentFrag(tempFrag);
                    
                    KHintApplication.LOGGER.log(tempFrag + " moved to " + getItem().getText());
                    checkAnswer(getItem());

                    // For some reason using getListView().refresh() was highlighting random rows
                    // Doing a copy and set seems to resolve this issue
                    List<DragTarget> itemscopy = new ArrayList<>(getListView().getItems());
                    getListView().getItems().setAll(itemscopy);

                    success = true;
                }

                event.setDropCompleted(success);
            	
                event.consume();
            });            

            setOnDragDone(DragEvent::consume);
        }
        
        /*
         * Checks if the answer the user placed here is the correct one
         * Postcondition1: If the answer is correct, log and move on.
         * Postcondition2: If the answer is incorrect, gather appropriate hints and present the user choices
         */
        private void checkAnswer(DragTarget target) {
        	if (target.getAnswer() == target.getCurrentFrag()) {
        		KHintApplication.LOGGER.log("Correct answer for " + target.getText() + " selected.");
        	} else {
        		KHintApplication.LOGGER.log("Incorrect answer for " + target.getText() + " selected, providing hint.");
        		ArrayList<Hint> theHints = new ArrayList<Hint>();  
    			
    			theHints.addAll(target.getCurrentFrag().getHints()); // Get hints for the current, wrong, fragment
    			theHints.addAll(target.getHints()); // Get hints for the target
    			
    			if (theHints.size() != 0 ) { // Make sure we have hints
    			
	    			Collections.sort(theHints); // Sorts by hint weight
	    			
	    			Hint low = theHints.get(0);
	    			
	    			KHintApplication.LOGGER.log("Selected low (" + low.getWeight() + ") hint: " + low);
	    			
	    			Hint mid = theHints.get(theHints.size()/2);  // Get one around the middle
	    			
	    			KHintApplication.LOGGER.log("Selected mid (" + mid.getWeight() + ") hint: " + mid);
	    			
	    			Hint high = theHints.get(theHints.size() - 1);  // The last hint should be the highest
	    			
	    			KHintApplication.LOGGER.log("Selected high (" + high.getWeight() + ") hint: " + high);
	    			
	    			Hint used = giveHint(low, mid, high);
	    			
	    			// Don't know which side this came from, so we'll just try and remove it from both
	    			if (used != null) {
	    				target.removeHint(used);
	    				target.getCurrentFrag().removeHint(used);
	    				
	    				// Calculate our new percentage
	    				int s = (int) ((myProblem.useHint(used) * 100.0) / myProblem.getTotalHintScore());
	    				
	    				score.setText(s + "%");

	    			}
    			} else {
    				KHintApplication.LOGGER.log("No hints left to give");
    				processHint(null);
    			}
        	}
        }
        
        /*
         * Presents a choice to the user of 3 levels of hints.
         * Postcondition: returns the hint that the user selected from the dialog box.
         */
        private Hint giveHint(Hint low, Hint mid, Hint high) {
        	Hint chosen = null;
        	ButtonType l = new ButtonType("Minor");
        	ButtonType m = new ButtonType("Medium");
        	ButtonType h = new ButtonType("Large");

        	Alert a = new Alert(AlertType.NONE, "", l, m, h);
        	
        	a.setTitle("Hint Selection");
        	
        	a.setHeaderText(null);
        	a.setResizable(false);
        	a.setContentText("Select a hint type.");
        	a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        	Optional<ButtonType> result = a.showAndWait();
        	if (result.get() == l){
        		KHintApplication.LOGGER.log("Used selected low: " + low.getText());
        	    chosen = low;
        	} else if (result.get() == m) {
        		KHintApplication.LOGGER.log("Used selected mediem: " + mid.getText());
        		chosen = low;
        	} else if (result.get() == h) {
        		KHintApplication.LOGGER.log("Used selected high: " + high.getText());
        		chosen = low;
        	}
        	
        	processHint(chosen);
        	
        	return chosen;
        }
        
        /*
         * Presents the hint information to the user
         * Postcondition: Information box shows the hint for the user
         */
        private void processHint(Hint h) {
        	Alert alert = new Alert(AlertType.INFORMATION);
    		alert.setTitle("Hint");
    		alert.setHeaderText(null);
    		
        	if (h != null)        		
        		alert.setContentText(h.getText());
        	else
        		alert.setContentText("Sorry, no more hints left for this fragment.");
        	alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        	alert.showAndWait();
        }
        
        @Override
        protected void updateItem(DragTarget item, boolean empty) {
            super.updateItem(item, empty);
            this.setWrapText(true);
            this.setPrefWidth(50.0);

            if (empty || item == null) {
            	setText(null);
            } else {            	
                setText(item.toString());
                if (item.isSolved()) {
                	setStyle("-fx-control-inner-background: green;");
                	// It's solved, so no more dragging and dropping here
                	this.setOnDragDetected(null);
                	this.setOnDragOver(null);
                	this.setOnDragDropped(null);
                } else {
                	setStyle("-fx-control-inner-background: white;");
                }
                	
            }
        }
    }

}