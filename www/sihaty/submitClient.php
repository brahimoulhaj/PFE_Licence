
<?php
			echo "loading...";

require('../sihaty/ConnectDB.php');
			$userId = $_POST["id"];
			$username = $_POST["name"];
			$usercin = $_POST["cin"];
			$usergsm = $_POST["gsm"];
			$userbirth = $_POST["birth"];
			$useremail = $_POST["email"];
			$useraddress = $_POST["address"];
			$photo = basename($_FILES["photo"]["name"]);

			$target_dir = "../sihaty/img/";
				$target_file = $target_dir . $photo;
				$uploadOk = 1;
				$imageFileType = strtolower(pathinfo($target_file,PATHINFO_EXTENSION));

				$check = getimagesize($_FILES["photo"]["tmp_name"]);
				if($check !== false) {
						echo "File is an image - " . $check["mime"] . ".";
						$uploadOk = 1;
				} else {
						echo "File is not an image.";
						$uploadOk = 0;
				}
				// Check file size
				if ($_FILES["photo"]["size"] > 1000000) {
						echo "Sorry, your file is too large.\n";
						$uploadOk = 0;
				}
				// Allow certain file formats
				if($imageFileType != "jpg" && $imageFileType != "png" && $imageFileType != "jpeg"
				&& $imageFileType != "JPG" && $imageFileType != "PNG" && $imageFileType != "JPEG" ) {
						echo "Sorry, only JPG, JPEG, PNG & GIF files are allowed.\n";
						$uploadOk = 0;
				}
				// Check if $uploadOk is set to 0 by an error
				if ($uploadOk == 0) {
					$response["success"] = false;
						echo "Sorry, your file was not uploaded.\n";
				// if everything is ok, try to upload file
				} else {
						if (move_uploaded_file($_FILES["photo"]["tmp_name"], $target_file)) {
								echo "The file ". basename( $_FILES["photo"]["name"]). " has been uploaded.";
						} else {
								echo "Sorry, there was an error uploading your file.\n";
							$response["success"] = false;
						}
				}

			if(!empty($_POST["email"])){
				$conn->exec("UPDATE patients
			SET nom_prenom='$username', cin='$usercin',
			telephone='$usergsm', date_naissance='$userbirth',
			adresse='$useraddress', email='$useremail', photo='$photo'
			WHERE id=$userId");
			}else{
				$conn->exec("UPDATE patients
			SET nom_prenom='$username', cin='$usercin',
			telephone='$usergsm', date_naissance='$userbirth',
			adresse='$useraddress', email=null, photo='$photo'
			WHERE id=$userId");
			}
?>

<script>
	
	window.location.href = "client.php";
</script>