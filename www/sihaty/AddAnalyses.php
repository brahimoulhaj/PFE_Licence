<?php include ("header.php");?>

<main>
<center>
	<div class="regContenaire">
		<div class="regHead">Ajouter des analyses</div>
		<div class="regForm">
			<form method=post action="SubmitAna.php" enctype="multipart/form-data">
				<input type="text" readonly hidden name="dossier_id" value="<?php echo $_POST["dos_id"]; ?>">
				<input type="file" name="file">
				<center><button class="button" type="submit" style="vertical-align:middel"><span>Ajouter </span></button></center>
			</form>
		</div>
	</div>
	</center>
</main>
<?php include ("end.php"); ?>

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
	.regForm input[type="file"]{
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