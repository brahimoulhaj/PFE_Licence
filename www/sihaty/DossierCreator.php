<?php include("header.php"); ?>
<main>
	<center>
	<div class="regContenaire">
		<div class="regHead"> Créer un dossier medical </div>
		<div class="regForm">
		<form method="post" action="DossierSubmit.php">
			<p>Spécialité:</p>
			<select name="specialite" required>
				<?php 
				require("../sihaty/ConnectDB.php");
				$data = $conn->query("SELECT * FROM specialites");
				while($donnee = $data->fetch(PDO::FETCH_ASSOC)){
					echo '<option value="'.$donnee["specialite"].'">'.$donnee["specialite"].'</option>';
				}
				?>
			</select>
			<p>Description:</p>
			<textarea rows="8" cols="70" name="description" placeholder="une petite description de ce dossier ici..." required></textarea>
			<br><br>CIN:
			<input type="text" name="cin" placeholder="N° de la carte national d'identité" required><br><br>
			<center><button class="button" type="submit" style="vertical-align:middel"><span>Créer </span></button></center>
		</form>
		</div>
	</div>
	</center>
</main>
<?php require('end.php'); ?>

<style>
	.regContenaire{
		width: 40%;
		text-align: center;
		background-color: white;
		border-radius: 7px;
		-webkit-box-shadow: 0px 0px 5px 0px rgba(0,0,0,0.75);
		-moz-box-shadow: 0px 0px 5px 0px rgba(0,0,0,0.75);
		box-shadow: 0px 0px 5px 0px rgba(0,0,0,0.75);
	}
	span.sexe{margin-left: 5px; padding-right: 50px; font-family: 'Open Sans'}
	.regHead{
		border-radius: 7px 7px 0 0;
		padding: 5%;
		font-size: 20px;
		font-family: 'Open Sans';
		color: cadetblue;
		background-color: aquamarine;
		margin: 0;
	}
	.regForm{
		text-align: left;
		padding: 5px;
	}
	.regForm input[type="text"]{
		font-family: 'Open Sans';
		width: 95%;
    padding: 1em;
    line-height: 1.4;
    background-color: #f9f9f9;
    border: 1px solid #e5e5e5;
    border-radius: 3px;
		margin-top: 5px;
}
	
	.regForm .button {
  display: inline-block;
  border-radius: 4px;
  color: black;
	background-color: aquamarine ;
  border: none;
  text-align: center;
  font-size: 22px;
  padding: 10px;
  width: 150px;
  transition: all 0.5s;
  cursor: pointer;
  margin: 5px;
}

.regForm .button span {
  cursor: pointer;
  display: inline-block;
  position: relative;
  transition: 0.5s;
}

.regForm .button span:after {
  content: '\00bb';
  position: absolute;
  opacity: 0;
  top: 0;
  right: -20px;
  transition: 0.5s;
}

.regForm .button:hover span {
  padding-right: 25px;
}

.regForm .button:hover span:after {
  opacity: 1;
  right: 0;
}
	
</style>
