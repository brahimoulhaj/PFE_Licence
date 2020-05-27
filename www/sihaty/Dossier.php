<?php include("header.php"); ?>
<script src="script.js"></script>
<main class="main_search">
	<center>
		<a href=DossierCreator.php>
			<div  id="register_btn"><i class="fa fa-plus-circle"></i>&nbsp;Créer un dossier</div>
		</a>
	</center>
	<center><?php include('DossierSearch.php'); ?></center>
	<br>
	<div class="dos_table"></div>
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