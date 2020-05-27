<?php include("header.php"); ?>
<script src="script.js"></script>
<main class="main_search">
	<center>
		<a href=register.php>
			<div  id="register_btn"><i class="fa fa-plus-circle"></i>&nbsp;Nouveau patient</div>
		</a>
	</center>
	<div class="patients_table"></div>
</main>
<?php require('end.php'); ?>

<style>
	#register_btn{
		transition: 0.5s;
		padding: 7px;
		background-color: aqua;
		width: 150px;
		margin-bottom: 10px;
		border-radius: 10px;
		color: black;
		font-family: 'Open Sans';
		font-weight: bold;
		display : flex;
  	align-items : center;
		text-align: center;
	}
	.fa-plus-circle{font-size: 20px;}
	#register_btn:hover{background-color: aquamarine; color:#555;}
</style>

<script type="text/javascript">
	function confirme(){
		<?php
			include('../sihaty/ConnectDB.php');
			$id = $_POST["user_id"];
			$conn->exec("UPDATE patients SET isConfirmed=1 WHERE id=$id");
		
			$user = $conn->query("SELECT * FROM patients WHERE id=$id");
		
			while($info = $user->fetch(PDO::FETCH_ASSOC)){
					$username = $info["nom_prenom"];
					$sexe = $info["sexe"]=="f"?"Mlle.":"M.";
			}
			
			$conn->exec("
				INSERT INTO patient_notif(title, text, patient_ID)
				VALUES('Confirmation','$sexe$username votre compte à été confirmer.',$id)
			");
		?>
	}
</script>

