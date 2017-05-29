import distutils.spawn
import os
import platform
import traceback


def file_present(file_name='not_passed', wbase='C:\\', ubase='/', exe=False):
    """
    Description: 
        The following is a function for searching for an file on the current system.
    Example usage:
    file_present(file_name="workflow-cli.exe", wbase="C:\\Users\\", exe=True)
    
    :param file_name: File name to be searched for.
    :param wbase: Base Windows directory to start search (Default: 'C:\\')
    :param ubase: Base Unix/Mac directory to start search (Default: '/')
    :param exe: Check if file is executable (Default: False)
    :return: str Path to where file has been found. False if not found.
    """
    try:
        file_name = str(file_name)
        os_system = platform.system()
        if os_system == "Windows":
            base_search = wbase
        else:
            base_search = ubase

        # Loops through directories on system start from base search directory (C:\ for windows / for unix and Mac)
        for root, dirs, files in os.walk(base_search, topdown=False):
            for name in files:
                # If executable(exe) = True, checks if file is executable, else only checks for file
                if exe:
                    # If file name found and is an executable returns Path to File
                    if file_name == name and distutils.spawn.find_executable(file_name, path=root):
                        return root
                else:
                    # If file name found  returns Path to File
                    if file_name == name:
                        return root
        print(file_name + " not found")
        return False

    except Exception as e:
        print("Unexpected error: " + str(e))
        traceback.print_exc()
        raise Exception(e)
        